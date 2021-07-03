package com.mdc.medic.apimedic.advice;

import com.mdc.medic.apimedic.security.jwt.exception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.EXPECTATION_FAILED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }
}
