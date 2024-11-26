package com.springboot.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.http.HttpResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    /**
     * 业务数据，如果状态码为200，则返回业务数据，否则为空
     */
    private T data;

    private boolean success;
    /**
     * 错误码，成功为200
     */
    private int status;
    /**
     * 错误信息，如果是200则为空
     */
    private String statusDescription;

    /**
     * 创建成功响应
     */
    public static <T> ApiResult<T> ok() {
        ApiResult<T> result = new ApiResult<>();
        result.setStatus(200);
        result.setSuccess(true);
        return result;
    }

    /**
     * 设置响应数据
     */
    public ApiResult<T> body(T data) {
        this.setData(data);
        return this;
    }

    /**
     * 创建错误响应
     */
    public static <T> ApiResult<T> error(String message) {
        ApiResult<T> result = new ApiResult<>();
        result.setStatus(500);
        result.setStatusDescription(message);
        return result;
    }

    /**
     * 创建指定错误码的错误响应
     */
    public static <T> ApiResult<T> error(int errorCode, String message) {
        ApiResult<T> result = new ApiResult<>();
        result.setStatus(errorCode);
        result.setStatusDescription(message);
        return result;
    }
}
