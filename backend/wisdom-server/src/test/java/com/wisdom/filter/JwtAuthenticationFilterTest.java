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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void tearDown() {
        BaseContext.removeCurrentId();
    }

    @Test
    void doFilterShouldSetContextWhenValidTokenProvided() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(1L);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterShouldNotSetContextWhenNoAuthorizationHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(BaseContext.getCurrentId());
    }

    @Test
    void doFilterShouldNotSetContextWhenTokenDoesNotStartWithBearer() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(BaseContext.getCurrentId());
    }

    @Test
    void doFilterShouldNotSetContextWhenTokenValidationFails() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtTokenUtil.validateToken("invalidToken")).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(BaseContext.getCurrentId());
    }

    @Test
    void doFilterShouldNotSetContextWhenGetUserIdReturnsNull() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validButNoUserId");
        when(jwtTokenUtil.validateToken("validButNoUserId")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validButNoUserId")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(BaseContext.getCurrentId());
    }

    @Test
    void doFilterShouldClearContextAfterFilterChain() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(1L);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(BaseContext.getCurrentId());
    }

    @Test
    void doFilterShouldClearContextEvenWhenFilterChainThrows() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtil.validateToken("validToken")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("validToken")).thenReturn(1L);
        doThrow(new ServletException("test")).when(filterChain).doFilter(request, response);

        assertThrows(ServletException.class, () -> filter.doFilter(request, response, filterChain));
        assertNull(BaseContext.getCurrentId());
    }
}
