package com.mdc.medic.apimedic.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenInterceptor implements HandlerInterceptor {
    Logger logger = LogManager.getLogger(getClass());

    private BearerTokenWrapper tokenWrapper;

    public BearerTokenInterceptor(BearerTokenWrapper tokenWrapper) {
        this.tokenWrapper = tokenWrapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            String token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
            tokenWrapper.setToken(token);
        }

        return true;
    }


}