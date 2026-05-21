package org.example.exceptions;

public class AssumptionException extends BasicRuntimeException {
    public AssumptionException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
