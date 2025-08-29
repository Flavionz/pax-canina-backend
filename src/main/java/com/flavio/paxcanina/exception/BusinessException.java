package com.flavio.paxcanina.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus status;
    private final Map<String, Object> details = new HashMap<>();

    public BusinessException(String errorCode, int statusCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.status = HttpStatus.valueOf(statusCode);
    }

    public BusinessException withDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }
}
