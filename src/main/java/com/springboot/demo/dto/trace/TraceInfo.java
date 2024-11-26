package com.springboot.demo.dto.trace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TraceInfo {

    private String traceId;

    private String spanId;

    private String parentSpanId;

    private String serviceName;

    private String serviceVersion;

    private Long startTime;
    private Long endTime;
    private Long costTime;

    private String requestBody;
    private String responseBody;

}
