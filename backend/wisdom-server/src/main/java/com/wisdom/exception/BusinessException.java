package com.wisdom.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public static BusinessException unauthorized() {
        return new BusinessException(401, "UNAUTHORIZED");
    }

    public static BusinessException forbidden() {
        return new BusinessException(403, "FORBIDDEN");
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }
}