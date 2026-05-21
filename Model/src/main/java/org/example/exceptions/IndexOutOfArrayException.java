package org.example.exceptions;

public class IndexOutOfArrayException extends BadFieldValueException {
    public IndexOutOfArrayException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
