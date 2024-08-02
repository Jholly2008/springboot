package com.springboot.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("springboot-demo/hello")
@Slf4j
public class HelloController {
    @GetMapping("/test-log")
    public String testLog() {
        // 写日志
        log.debug("Write debug log...");
        log.warn("Write warn log...");
        log.info("Write info log...");
        log.error("Write error log...");
        return "Log write ...";
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
