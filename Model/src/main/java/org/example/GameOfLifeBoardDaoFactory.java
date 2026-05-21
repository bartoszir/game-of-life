package org.example;

public class GameOfLifeBoardDaoFactory {
    public Dao<GameOfLifeBoard> getFileDao(String fileName) {
        return new FileGameOfLifeBoardDao(fileName);
    }
}
