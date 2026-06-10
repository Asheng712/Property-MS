package com.wisdom.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.annotation.LoginRequired;
import com.wisdom.context.BaseContext;
import com.wisdom.result.Result;
import com.wisdom.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private RequestMappingHandlerMapping handlerMapping;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HandlerExecutionChain handlerChain;

    @Mock
    private HandlerMethod handlerMethod;

    @InjectMocks
    private AuthenticationFilter filter;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        lenient().when(response.getWriter()).thenReturn(printWriter);
    }

    @AfterEach
    void tearDown() {
        BaseContext.removeCurrentId();
    }

    // --- White list tests ---

    @Test
    void doFilterShouldAllowLoginUriWithoutToken() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/api/v1/auth/login");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldAllowRegisterUriWithoutToken() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/api/v1/auth/register");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldAllowSwaggerUriWithoutToken() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldAllowKnife4jUriWithoutToken() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/knife4j/docs");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldAllowApiDocsUriWithoutToken() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/v3/api-docs/swagger-config");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    // --- Non-whitelist path without LoginRequired ---

    @Test
    void doFilterShouldPassThroughWhenHandlerMappingFails() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/some-path");
        when(handlerMapping.getHandler(request)).thenThrow(new RuntimeException("mapping error"));

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldPassThroughWhenNoHandlerFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/non-existent");
        when(handlerMapping.getHandler(request)).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldPassThroughWhenHandlerIsNotHandlerMethod() throws Exception {
        // Simulate a case where getHandler returns a non-HandlerMethod object
        // This can happen with resource handlers
        when(request.getRequestURI()).thenReturn("/static/resource");
        // handlerMapping.getHandler returns null, which triggers the null check
        when(handlerMapping.getHandler(request)).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    // --- Non-whitelist path WITH LoginRequired ---

    @Test
    void doFilterShouldAllowWhenValidTokenProvidedForLoginRequiredPath() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(1L);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldRejectWhenNoAuthorizationHeaderOnLoginRequiredPath() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"code\":401,\"msg\":\"UNAUTHORIZED\"}");

        filter.doFilter(request, response, filterChain);

        verify(response).setStatus(401);
        verify(filterChain, never()).doFilter(any(), any());
        assertTrue(stringWriter.toString().contains("UNAUTHORIZED"));
    }

    @Test
    void doFilterShouldRejectWhenTokenDoesNotStartWithBearerOnLoginRequiredPath() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn("Basic badToken");
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"code\":401,\"msg\":\"UNAUTHORIZED\"}");

        filter.doFilter(request, response, filterChain);

        verify(response).setStatus(401);
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void doFilterShouldRejectWhenTokenValidationFailsOnLoginRequiredPath() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtTokenUtil.validateToken("invalidToken")).thenReturn(false);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"code\":401,\"msg\":\"INVALID_TOKEN\"}");

        filter.doFilter(request, response, filterChain);

        verify(response).setStatus(401);
        verify(filterChain, never()).doFilter(any(), any());
        assertTrue(stringWriter.toString().contains("INVALID_TOKEN"));
    }

    @Test
    void doFilterShouldRejectWhenGetUserIdReturnsNullOnLoginRequiredPath() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"code\":401,\"msg\":\"UNAUTHORIZED\"}");

        filter.doFilter(request, response, filterChain);

        verify(response).setStatus(401);
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void doFilterShouldCleanUpContextAfterSuccessfulFilterChain() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(1L);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(BaseContext.getCurrentId());
    }

    @Test
    void doFilterShouldCleanUpContextWhenFilterChainThrowsException() throws Exception {
        Method dummyMethod = DummyController.class.getMethod("securedMethod");
        when(handlerMethod.getMethod()).thenReturn(dummyMethod);
        when(handlerChain.getHandler()).thenReturn(handlerMethod);
        when(request.getRequestURI()).thenReturn("/api/v1/secure/resource");
        when(handlerMapping.getHandler(request)).thenReturn(handlerChain);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(1L);
        doThrow(new ServletException("chain error")).when(filterChain).doFilter(request, response);

        assertThrows(ServletException.class, () -> filter.doFilter(request, response, filterChain));
        assertNull(BaseContext.getCurrentId());
    }

    // --- Dummy controller for annotations ---
    @SuppressWarnings("unused")
    static class DummyController {
        @LoginRequired
        public void securedMethod() {
        }

        public void unsecuredMethod() {
        }
    }
}
