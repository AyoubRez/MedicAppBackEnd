package com.mdc.medic.apimedic.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger log = LogManager.getLogger(getClass());

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        String warnMsg = "Unauthorized access to requested services" + (username != null ? " for user '" + username + "'" : "");
        return handle(ex, warnMsg, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InsufficientAuthenticationException.class)
    protected ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, WebRequest request) {
        return handle(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = FunctionalException.class)
    protected ResponseEntity<Object> handleBWFunctionalException(FunctionalException ex, WebRequest request) {
        return handle(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = TechnicalException.class)
    protected ResponseEntity<Object> handleTechnicalException(TechnicalException ex, WebRequest request) {
        return handle(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handle(Exception ex, WebRequest request, HttpStatus status) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handle(Exception ex, String body, WebRequest request, HttpStatus status) {
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationError error = ValidationErrorBuilder.fromBindingErrors(ex.getBindingResult());
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        switch (status) {
            case BAD_REQUEST:
                log.warn("BAD_REQUEST error " + ex.getLocalizedMessage());
                break;
            case UNAUTHORIZED:
                log.warn("UNAUTHORIZED error " + ex.getLocalizedMessage());
                break;
            case INTERNAL_SERVER_ERROR:
                log.warn("INTERNAL_SERVER_ERROR error ", ex);
                break;
            default:
                log.error("Default error ", ex);
                break;
        }
        Object bodyObject;
        /*
        if (body instanceof String) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", body);
            bodyObject = map;
        } else {
            bodyObject = body;
        }
        */

        if (ex instanceof FunctionalException) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", ((FunctionalException) ex).getCode());
            map.put("error", ex.getMessage());
            map.put("exception", ex.getClass().getSimpleName());
            bodyObject = map;

        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("exception", "Error");
            map.put("code", MedicFrontExceptionCode.DEFAULT_ERROR);
            map.put("error", "Une erreur est survenue");
            bodyObject = map;
        }

        return super.handleExceptionInternal(ex, bodyObject, headers, status, request);
    }

}
