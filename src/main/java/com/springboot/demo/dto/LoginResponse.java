package com.springboot.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "登录结果")
public class LoginResponse {
    @Schema(description = "状态", example = "ok")
    private String status;
    @Schema(description = "类型", example = "account")
    private String type;
    @Schema(description = "token", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String currentAuthority;
    @Schema(description = "版本", example = "v2")
    private String frontendVersion;
}
