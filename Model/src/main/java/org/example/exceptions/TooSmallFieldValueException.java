package org.example.exceptions;

public class TooSmallFieldValueException extends BadFieldValueException {
    public TooSmallFieldValueException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
