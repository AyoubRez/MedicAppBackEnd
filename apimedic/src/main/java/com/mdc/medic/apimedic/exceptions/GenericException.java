package com.mdc.medic.apimedic.exceptions;

import javax.servlet.ServletException;

public class GenericException extends ServletException {

    private String code;

    public GenericException(String code) {
        super();
        this.code = code;
    }

    public GenericException(String code, String message) {
        super(message);
        this.code = code;
    }

    public GenericException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public GenericException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
