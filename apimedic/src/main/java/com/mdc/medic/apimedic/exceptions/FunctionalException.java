package com.mdc.medic.apimedic.exceptions;

public class FunctionalException extends GenericException {

    public FunctionalException(String code) {
        super(code);
    }

    public FunctionalException(String code, String message) {
        super(code, message);
    }

    public FunctionalException(String code, Throwable cause) {
        super(code, cause);
    }

    public FunctionalException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
