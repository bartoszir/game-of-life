package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameOfLifeBoardDaoFactoryTest {
    private GameOfLifeBoardDaoFactory factory;
    private GameOfLifeSimulator simulator;
    private GameOfLifeBoard prototype;

    @BeforeEach
    public void setUp() {
        factory = new GameOfLifeBoardDaoFactory();
        simulator = new PlainGameOfLifeSimulator();
        prototype = new GameOfLifeBoard(5, 5, 5, simulator);
    }

    @Test
    public void getFileDaoTest() {
        assertNotNull(factory.getFileDao("abc"));
    }

    @Test
    public void registerPrototypeTest() {
        // register prototype
        factory.registerPrototype("testBoard", prototype);

        // create an instance of board
        try {
            GameOfLifeBoard testBoard = factory.createInstance("testBoard");

            assertNotNull(testBoard);
            assertNotSame(prototype, testBoard);
            assertEquals(prototype.getNumRows(), testBoard.getNumRows());
            assertEquals(prototype.getNumCols(), testBoard.getNumCols());
            assertTrue(prototype.equals(testBoard));
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void registerPrototypeInvalidKeyTest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createInstance("invalidKey");
        });
        assertEquals("No prototype registered with key: invalidKey", exception.getMessage());
    }

    @Test
    public void unregisterPrototypeTest() {
        factory.registerPrototype("testBoard", prototype);
        factory.unregisterPrototype("testBoard");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createInstance("testBoard");
        });
        assertEquals("No prototype registered with key: testBoard", exception.getMessage());
    }

}
