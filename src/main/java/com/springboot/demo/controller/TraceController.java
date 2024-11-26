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

    private final OkHttpClient okHttpClient;

    private static final String X_B3_TRACE_ID = "X-B3-TraceId";
    private static final String X_B3_SPAN_ID = "X-B3-SpanId";
    private static final String X_B3_PARENT_SPAN_ID = "X-B3-ParentSpanId";
    private static final String X_B3_SAMPLED = "X-B3-Sampled";

    public TraceController(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    private String generateSpanId() {
        return "service-a-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @GetMapping("/trace")
    public PageResult<Object> testSimulateIOOperation(HttpServletRequest httpServletRequest) {
        // 从网关获取追踪信息
        String traceId = httpServletRequest.getHeader(X_B3_TRACE_ID);
        String gatewaySpanId = httpServletRequest.getHeader(X_B3_SPAN_ID); // 网关的spanId作为我们的parentSpanId
        String gatewayParentSpanId = httpServletRequest.getHeader(X_B3_PARENT_SPAN_ID); // 网关的spanId作为我们的parentSpanId
        String sampled = httpServletRequest.getHeader(X_B3_SAMPLED);

        if (traceId == null) {
            log.warn("No trace ID found in request, generating new one");
            traceId = UUID.randomUUID().toString().replace("-", "");
            gatewaySpanId = "0"; // 如果没有网关spanId，设置为0
            sampled = "1";
        }

        // 为Service A生成新的spanId
        String serviceASpanId = generateSpanId();

        long startTime = System.currentTimeMillis();

        // 调用Service B的请求
        Request request = new Request.Builder()
                .url(serviceBUrl + "/api/cost")
                .get()
                .addHeader(X_B3_TRACE_ID, traceId)
                .addHeader(X_B3_PARENT_SPAN_ID, serviceASpanId)  // Service A的spanId作为Service B的parentSpanId
                .addHeader(X_B3_SPAN_ID, generateSpanId())  // 为Service B生成新的spanId
                .addHeader(X_B3_SAMPLED, sampled)
                .build();

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
                    gatewaySpanId,
                    gatewayParentSpanId,
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
                    gatewaySpanId, // 网关的spanId作为Service A的parentSpanId
                    "serviceA",
                    "1.0.0",
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