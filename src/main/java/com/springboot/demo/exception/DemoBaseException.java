package com.springboot.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class DemoBaseException extends RuntimeException {

    private String errorCode = ErrorCode.DemoBaseException_ErrorCode;

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public DemoBaseException() {
    }

    public DemoBaseException(String message) {
        super(message);
    }

    public DemoBaseException(Throwable cause) {
        super(cause);
    }

    public DemoBaseException(String errorCode, HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public DemoBaseException(String errorCode, HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}
