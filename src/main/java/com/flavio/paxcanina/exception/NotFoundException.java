package com.flavio.paxcanina.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String code) {
        super(code, 404);
    }
}
