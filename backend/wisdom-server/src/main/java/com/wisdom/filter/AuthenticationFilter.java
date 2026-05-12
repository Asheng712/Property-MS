package com.wisdom.filter;

import com.wisdom.annotation.LoginRequired;
import com.wisdom.context.BaseContext;
import com.wisdom.result.Result;
import com.wisdom.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
@Order(2)
public class AuthenticationFilter implements Filter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String[] WHITE_LIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/api-docs/",
            "/v3/api-docs/",
            "/knife4j/"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();

        if (isWhiteList(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        if (requiresLogin(httpRequest)) {
            String token = httpRequest.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                sendErrorResponse(httpResponse, 401, "UNAUTHORIZED");
                return;
            }

            token = token.substring(7);
            if (!jwtTokenUtil.validateToken(token)) {
                sendErrorResponse(httpResponse, 401, "INVALID_TOKEN");
                return;
            }

            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            if (userId == null) {
                sendErrorResponse(httpResponse, 401, "UNAUTHORIZED");
                return;
            }
            BaseContext.setCurrentId(userId);
        }

        chain.doFilter(request, response);
        BaseContext.removeCurrentId();
    }

    private boolean isWhiteList(String uri) {
        for (String path : WHITE_LIST) {
            if (uri.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    private boolean requiresLogin(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);
            if (handlerChain != null && handlerChain.getHandler() instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handlerChain.getHandler();
                Method method = handlerMethod.getMethod();
                return method.isAnnotationPresent(LoginRequired.class);
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}