package org.example.exceptions;

public class MethodNotFoundException extends BasicRuntimeException {
    public MethodNotFoundException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
