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
@Schema(description = "登录请求")
public class LoginRequest {
    @Schema(description = "密码", example = "123456")
    private String password;
    @Schema(description = "用户名", example = "kongxj")
    private String username;
}
