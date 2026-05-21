package org.example.exceptions;

public class BadFieldValueException extends BasicRuntimeException {
    public BadFieldValueException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
