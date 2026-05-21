package org.example;

import org.example.exceptions.DatabaseConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class GameOfLifeBoardDaoFactoryTest {
    private GameOfLifeBoardDaoFactory factory;

    @BeforeEach
    public void setUp() {
        Locale.setDefault(new Locale("en", "EN"));
        factory = new GameOfLifeBoardDaoFactory();
    }

    @Test
    public void getFileDaoTest() {
        assertNotNull(factory.getFileDao("abc"));
    }

    @Test
    public void getJdbcDaoTest() {
        try {
            factory.getJdbcDao("board");
        } catch (DatabaseConnectionException e) {
            assertEquals("Error reading from the database.", e.getMessage());
        }
    }
}
