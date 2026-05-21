package org.example;

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
}
