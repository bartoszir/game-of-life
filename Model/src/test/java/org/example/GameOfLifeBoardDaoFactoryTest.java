package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameOfLifeBoardDaoFactoryTest {
    private GameOfLifeBoardDaoFactory factory = new GameOfLifeBoardDaoFactory();

    @Test
    public void getFileDaoTest() {
        assertNotNull(factory.getFileDao("abc"));
    }
}
