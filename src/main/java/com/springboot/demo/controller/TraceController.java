package com.springboot.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.demo.dto.trace.TraceInfo;
import com.springboot.demo.response.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "链路", description = "链路")
@RestController
@RequestMapping("/service-a")
@Slf4j
public class TraceController {
    @Value("${service-b.url}")
    private String serviceBUrl;
    @Value("${app.version}")
    private String version;
    @Value("${app.kubernetes.enabled:false}")
    private boolean isKubernetesEnabled;
    private final OkHttpClient okHttpClient;

    private static final String X_B3_TRACE_ID = "X-B3-TraceId";
    private static final String X_B3_SPAN_ID = "X-B3-SpanId";
    private static final String X_B3_PARENT_SPAN_ID = "X-B3-ParentSpanId";
    private static final String X_B3_SAMPLED = "X-B3-Sampled";

    public TraceController(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    private String generateSpanId(HttpServletRequest request) {
        if (isKubernetesEnabled) {
            String istioSpanId = request.getHeader(X_B3_SPAN_ID);
            log.debug("Using Istio generated spanId: {}", istioSpanId);
            return istioSpanId;
        } else {
            String newSpanId = "service-a-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            log.debug("Generated new spanId: {}", newSpanId);
            return newSpanId;
        }
    }

    private String getParentSpanId(HttpServletRequest request) {
        if (isKubernetesEnabled) {
            String parentSpanId = request.getHeader(X_B3_PARENT_SPAN_ID);
            log.info("Using Istio generated parentSpanId: {}", parentSpanId);
            return parentSpanId;
        } else {
            String parentSpanId = request.getHeader(X_B3_SPAN_ID);
            log.info("Using Gateway generated parentSpanId: {}", parentSpanId);
            return parentSpanId;
        }
    }

    private void logTraceInfo(HttpServletRequest request) {
        String traceId = request.getHeader(X_B3_TRACE_ID);
        String spanId = request.getHeader(X_B3_SPAN_ID);
        String parentSpanId = request.getHeader(X_B3_PARENT_SPAN_ID);

        log.info("Gateway Trace - ParentSpanId: {}, TraceId: {}, SpanId: {}", parentSpanId, traceId, spanId);
    }

    @GetMapping("/trace")
    public PageResult<Object> testSimulateIOOperation(HttpServletRequest httpServletRequest) {
        logTraceInfo(httpServletRequest);


        // 从网关获取追踪信息
        String traceId = httpServletRequest.getHeader(X_B3_TRACE_ID);
        String sampled = httpServletRequest.getHeader(X_B3_SAMPLED);

        // 为Service A生成新的spanId
        String parentSpanId = getParentSpanId(httpServletRequest);
        String serviceASpanId = generateSpanId(httpServletRequest);

        long startTime = System.currentTimeMillis();

        // 构建请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(serviceBUrl + "/api/cost")
                .get();

        log.info("parentSpanId:" + parentSpanId + " traceId:" + traceId + "   sampled:" + sampled + "   serviceASpanId:" + serviceASpanId);
        // 只在非 k8s 环境下手动传递追踪信息
        if (!isKubernetesEnabled) {
            log.debug("Not in Kubernetes environment, manually propagating trace headers");
            requestBuilder
                    .addHeader(X_B3_TRACE_ID, traceId)
                    .addHeader(X_B3_PARENT_SPAN_ID, parentSpanId)
                    .addHeader(X_B3_SPAN_ID, serviceASpanId)
                    .addHeader(X_B3_SAMPLED, sampled);
        } else {
            log.info("Running in Kubernetes environment, trace propagation handled by Istio");
        }

        Request request = requestBuilder.build();

        List<TraceInfo> traceInfoList = new ArrayList<>();

        try {
            Response response = okHttpClient.newCall(request).execute();
            long endTime = System.currentTimeMillis();

            if (!response.isSuccessful()) {
                log.error("调用服务B失败, 状态码: {}", response.code());
                throw new RuntimeException("Service B call failed with status: " + response.code());
            }

            String responseBody = response.body().string();
            log.info("服务B响应: {}", responseBody);

            // 构建网关的TraceInfo
            TraceInfo gatewayTrace = new TraceInfo(
                    traceId,
                    parentSpanId, // 对service而言，网关的spanId就是我service的parentSpanId
                    "0",
                    "gateway",
                    "1.0.0",
                    startTime - 100, // 假设网关比Service A早100ms
                    startTime,
                    100L,
                    null,
                    null
            );

            // 构建Service A的TraceInfo
            TraceInfo serviceATrace = new TraceInfo(
                    traceId,
                    serviceASpanId,
                    parentSpanId, // 网关的spanId作为Service A的parentSpanId
                    "serviceA",
                    version,
                    startTime,
                    endTime,
                    endTime - startTime,
                    request.toString(),
                    responseBody
            );

            // 解析Service B的trace信息
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJson = mapper.readTree(responseBody);
            JsonNode traceBInfo = responseJson.get("trace_info");

            TraceInfo serviceBTrace = new TraceInfo(
                    traceBInfo.get("traceId").asText(),
                    traceBInfo.get("spanId").asText(),
                    traceBInfo.get("parentSpanId").asText(),
                    traceBInfo.get("serviceName").asText(),
                    traceBInfo.get("version").asText(),
                    traceBInfo.get("startTime").asLong(),
                    traceBInfo.get("endTime").asLong(),
                    traceBInfo.get("costTime").asLong(),
                    traceBInfo.get("requestBody").asText(),
                    traceBInfo.get("responseBody").toString()
            );

            // 按调用顺序添加trace信息
            traceInfoList.add(gatewayTrace);
            traceInfoList.add(serviceATrace);
            traceInfoList.add(serviceBTrace);

        } catch (Exception e) {
            log.error("调用服务B异常", e);
            throw new RuntimeException("Error calling Service B", e);
        }

        return PageResult.ok()
                .body(traceInfoList)
                .pagination(1, 10, traceInfoList.size());
    }


}