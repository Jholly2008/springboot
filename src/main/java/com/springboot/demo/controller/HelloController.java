package com.springboot.demo.controller;

import io.opentelemetry.api.baggage.Baggage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/service-a")
@Slf4j
@Tag(name = "测试", description = "测试")
public class HelloController {

    private static final String TENANT_HEADER = "x-api-version";

    @Value("${app.version}")
    private String version;

    @GetMapping("/test-log")
    public String testLog() {
        // 写日志
        log.debug("Write debug log...");
        log.warn("Write warn log...");
        log.info("Write info log...");
        log.error("Write error log...");
        return "Log write ...";
    }

    @GetMapping("/version")
    public String version(@RequestHeader Map<String, String> headers) {
        log.error("version..." + version);
        System.out.println("version..." + version);

        // 打印所有请求头
        System.out.println("=== Headers ===");
        headers.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        // 特别打印 baggage 相关信息
        String tenant = Baggage.current().getEntryValue(TENANT_HEADER);
        System.out.println("\n=== Baggage ===");
        System.out.println("tenant: " + tenant);

        // 打印当前所有 Baggage 值
        System.out.println("\n=== All Baggage Values ===");
        Baggage.current().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        return "version ..." + version;
    }

    @GetMapping("/test-io")
    public String testSimulateIOOperation() {
        // 模拟I/O操作
        simulateIOOperation();
        return "Test response with computation and I/O";
    }

    @GetMapping("/test-air")
    public String testEndpoint() {
        // 迅捷的响应
        return "Test response with air";
    }

    @PostMapping("/test-large")
    public String postTest(@RequestBody String largePayload) {
        // 模拟处理大报文
        return "Received " + largePayload.length() + " characters" + " , request : " + largePayload;
    }

    @PostMapping("/test-file-upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return file.getName() + file.getOriginalFilename();
    }

    @GetMapping("/test-time-out")
    public String testTimeOut() throws InterruptedException {
        // 模拟超时
        Thread.sleep(120000);
        return "Test response with sleep";
    }

    private void simulateIOOperation() {
        try {
            // 模拟I/O延迟
            Thread.sleep(100); // 模拟100ms的I/O延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
