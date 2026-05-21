package org.example;

public class GameOfLifeBoardDaoFactory {
    Dao<GameOfLifeBoard> getFileDao(String fileName)  {
        return new FileGameOfLifeBoardDao(fileName);
    }
}
