package com.wisdom.handler;

import com.wisdom.exception.BusinessException;
import com.wisdom.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void businessExceptionHandlerShouldReturnCode500WithMessage() {
        BusinessException ex = BusinessException.notFound("USER_NOT_FOUND");

        ResponseEntity<Result<Void>> response = handler.businessExceptionHandler(ex);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("USER_NOT_FOUND", response.getBody().getMsg());
        assertNull(response.getBody().getData());
    }

    @Test
    void businessExceptionHandlerShouldPreserveMessageForBadRequest() {
        BusinessException ex = BusinessException.badRequest("PASSWORD_ERROR");

        ResponseEntity<Result<Void>> response = handler.businessExceptionHandler(ex);

        assertEquals(500, response.getBody().getCode());
        assertEquals("PASSWORD_ERROR", response.getBody().getMsg());
    }

    @Test
    void validationExceptionHandlerShouldReturnCombinedFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("userLoginDTO", "username", "用户名不能为空");
        FieldError fieldError2 = new FieldError("userLoginDTO", "password", "密码长度至少6位");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Result<Void>> response = handler.validationExceptionHandler(ex);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("username: 用户名不能为空, password: 密码长度至少6位", response.getBody().getMsg());
    }

    @Test
    void validationExceptionHandlerShouldHandleSingleFieldError() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("dto", "email", "邮箱格式不正确");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Result<Void>> response = handler.validationExceptionHandler(ex);

        assertEquals("email: 邮箱格式不正确", response.getBody().getMsg());
    }

    @Test
    void httpMessageNotReadableExceptionHandlerShouldReturnBadRequestMessage() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Malformed JSON");

        ResponseEntity<Result<Void>> response = handler.httpMessageNotReadableExceptionHandler(ex);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("请求体格式错误", response.getBody().getMsg());
    }

    @Test
    void runtimeExceptionHandlerShouldReturnMessageAsIs() {
        RuntimeException ex = new RuntimeException("SERVICE_CALL_FAILED");

        ResponseEntity<Result<Void>> response = handler.runtimeExceptionHandler(ex);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("SERVICE_CALL_FAILED", response.getBody().getMsg());
    }

    @Test
    void exceptionHandlerShouldReturnInternalServerErrorWithGenericMessage() {
        Exception ex = new Exception("Native database connection failure");

        ResponseEntity<Result<Void>> response = handler.exceptionHandler(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("服务器内部错误", response.getBody().getMsg());
    }
}
