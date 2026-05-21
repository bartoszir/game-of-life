package org.example.exceptions;

public class DatabaseConnectionException extends BasicRuntimeException {
  public DatabaseConnectionException(String messageKey, Throwable cause) {
    super(messageKey, cause);
  }
}
