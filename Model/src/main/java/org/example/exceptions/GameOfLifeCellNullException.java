package org.example.exceptions;

public class GameOfLifeCellNullException extends BasicRuntimeException {
    public GameOfLifeCellNullException(String messageKey, Throwable cause) {
      super(messageKey, cause);
    }
}
