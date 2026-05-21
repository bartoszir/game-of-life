package org.example;

import org.example.exceptions.DatabaseConnectionException;

public class GameOfLifeBoardDaoFactory {
    public Dao<GameOfLifeBoard> getFileDao(String fileName) {
        return new FileGameOfLifeBoardDao(fileName);
    }

    public Dao<GameOfLifeBoard> getJdbcDao(String boardName) {
        try {
            return new JdbcGameOfLifeBoardDao(boardName);
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
