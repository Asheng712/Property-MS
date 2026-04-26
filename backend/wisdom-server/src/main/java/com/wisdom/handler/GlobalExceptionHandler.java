package com.wisdom.handler;

import com.wisdom.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @RequestMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.error("Bad request payload: {}", ex.getMessage(), ex);
        return Result.error("BAD_REQUEST");
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(produces = "application/json")
    public Result<Void> exceptionHandler(Exception ex) {
        log.error("System exception: {}", ex.getMessage(), ex);
        return Result.error("SERVER_BUSY");
    }
}
