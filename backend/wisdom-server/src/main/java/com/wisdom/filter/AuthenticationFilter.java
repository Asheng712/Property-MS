package com.wisdom.filter;

import com.wisdom.annotation.LoginRequired;
import com.wisdom.context.BaseContext;
import com.wisdom.result.Result;
import com.wisdom.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthenticationFilter implements Filter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ObjectMapper objectMapper;

    /** 静态资源和白名单路径——始终放行 */
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

        // 白名单：公开接口 + 静态资源
        if (isWhiteList(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        // 非白名单路径：判定是否需要登录
        if (!requiresLogin(httpRequest)) {
            // 静态资源或内部转发，放行
            chain.doFilter(request, response);
            return;
        }

        // 需要登录：校验 token
        String token = httpRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            sendErrorResponse(httpResponse, 401, "UNAUTHORIZED");
            return;
        }

        token = token.substring(7);
        if (!jwtTokenUtil.validateToken(token)) {
            log.warn("Invalid token for URI: {}", requestUri);
            sendErrorResponse(httpResponse, 401, "INVALID_TOKEN");
            return;
        }

        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        if (userId == null) {
            sendErrorResponse(httpResponse, 401, "UNAUTHORIZED");
            return;
        }
        BaseContext.setCurrentId(userId);

        try {
            chain.doFilter(request, response);
        } finally {
            BaseContext.removeCurrentId();
        }
    }

    private boolean isWhiteList(String uri) {
        for (String path : WHITE_LIST) {
            if (uri.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定当前请求是否需要登录。
     * 白名单模式：仅 @LoginRequired 标注的方法需要鉴权。
     * 未标注的、静态资源等均不需要登录。
     */
    private boolean requiresLogin(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);
            if (handlerChain == null) {
                return false;
            }
            if (handlerChain.getHandler() instanceof HandlerMethod handlerMethod) {
                Method method = handlerMethod.getMethod();
                return method.isAnnotationPresent(LoginRequired.class);
            }
            return false;
        } catch (Exception e) {
            log.debug("Handler mapping failed for: {}", request.getRequestURI(), e);
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
