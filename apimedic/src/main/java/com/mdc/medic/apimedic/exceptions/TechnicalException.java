package com.mdc.medic.apimedic.exceptions;

public class TechnicalException extends GenericException {

    public TechnicalException(String code) {
        super(code);
    }

    public TechnicalException(String code, String message) {
        super(code, message);
    }

    public TechnicalException(String code, Throwable cause) {
        super(code, cause);
    }

    public TechnicalException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
