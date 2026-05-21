package org.example.exceptions;

public class GameOfLifeDaoException extends BasicException {
    public GameOfLifeDaoException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}
