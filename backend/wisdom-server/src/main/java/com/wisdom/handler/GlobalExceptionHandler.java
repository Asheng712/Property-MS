package com.wisdom.handler;

import com.wisdom.exception.BusinessException;
import com.wisdom.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> businessExceptionHandler(BusinessException ex) {
        log.warn("Business exception: code={}, message={}", ex.getCode(), ex.getMessage());
        HttpStatus status = HttpStatus.valueOf(ex.getCode());
        return ResponseEntity.status(status).body(Result.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> validationExceptionHandler(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation exception: {}", message);
        return ResponseEntity.badRequest().body(Result.error(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.error("Bad request payload: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Result.error("请求体格式错误"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<Void>> runtimeExceptionHandler(RuntimeException ex) {
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        if ("UNAUTHORIZED".equals(ex.getMessage())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error("UNAUTHORIZED"));
        } else if ("FORBIDDEN".equals(ex.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error("FORBIDDEN"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error("服务器内部错误"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> exceptionHandler(Exception ex) {
        log.error("System exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error("服务器内部错误"));
    }
}