package com.springboot.demo.exception;

/**
 * 错误码规划
 */
public interface ErrorCode {
    // 基础异常从190781000开始，到190781099结束
    final static String DemoBaseException_ErrorCode = "10001000";

    // 认证失败
    final static String UnauthorizedException_ErrorCode = "10001001";


}
