package com.wisdom.handler;

import com.wisdom.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @RequestMapping(produces = "application/json")
    public Result exceptionHandler(Exception ex) {
        log.error("系统异常: {}", ex.getMessage());
        return Result.error("服务器繁忙，请稍后再试");
    }

}