package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeBoardTest {
    int testNumRows = 11;
    int testNumCols = 11;
    // int testNumberOfMaxIterations = 3;
    int testNumberOfLiveCells = 15;
    GameOfLifeSimulator testGameSimulator;
    GameOfLifeBoard testGame;
    boolean[][] testCleanBoard;

    @BeforeEach
    void setUp() {
        testGameSimulator = new PlainGameOfLifeSimulator();
        testGame = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, testGameSimulator);
    }

    @Test
    public void testConstructorValidInputs() {

        assertEquals(testNumRows, testGame.getNumRows());
        assertEquals(testNumCols, testGame.getNumCols());
    }

    @Test
    void testConstructorInvalidInputs() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GameOfLifeBoard(0, testNumCols, testNumberOfLiveCells, testGameSimulator);
        });
        assertEquals("Number of rows must be greater than 0", exception.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new GameOfLifeBoard(testNumRows, 0, testNumberOfLiveCells, testGameSimulator);
        });
        assertEquals("Number of columns must be greater than 0", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            new GameOfLifeBoard(testNumRows, testNumCols, 0, testGameSimulator);
        });
        assertEquals("Number of life cells must be greater than 0", exception3.getMessage());

        int testTooMuchLiveCells = testNumRows * testNumCols + 1;
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            new GameOfLifeBoard(testNumRows, testNumCols, testTooMuchLiveCells, testGameSimulator);
        });
        assertEquals("Number of life cells can't be be greater than number of all cells", exception4.getMessage());

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, null);
        });
        assertEquals("GameOfLifeSimulator cannot be null", exception5.getMessage());
    }

    @Test
    public void testInitialSetup() {

        GameOfLifeBoard testGame2 = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, testGameSimulator);

        //deepEquals
        boolean areBoardsDifferent = Objects.deepEquals(testGame, testGame2);
        assertFalse(areBoardsDifferent);
    }

    @Test
    public void testCountAliveNeighbors() {

        int testNumRows = testGame.getNumRows();
        int testNumCols = testGame.getNumCols();

        boolean[][] testBoard = new boolean[testNumRows][testNumCols];

        testBoard[5][5] = true; //this the cell that we are going to check after doStep()
        testBoard[4][5] = true;
        testBoard[4][6] = true;
        testBoard[5][6] = true;
        testBoard[6][6] = true;

        testGame.setGameBoard(testBoard);

        int count = testGame.countAliveNeighbours(5, 5);

        assertEquals(4, count, "the cell at [5,5] should have 4 neighbours");
    }


    @Test
    public void testSetter() {
        int testRowNumber = 5;
        int testColNumber = 5;
        boolean testState = true;

        testCleanBoard = new boolean[testNumRows][testNumCols];
        testGame.setGameBoard(testCleanBoard);

        assertFalse(testGame.get(testRowNumber, testColNumber));

        testGame.set(testRowNumber, testColNumber, testState);

        assertTrue(testGame.get(testRowNumber, testColNumber));
    }

    @Test
    public void testSetterInvalidInputs() {
        //incorrect inputs for row
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testGame.set(-1, testNumCols - 1, true);
        });
        assertEquals("Number of row is out of array.", exception.getMessage());

        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.set(testNumRows * testNumCols, testNumCols - 1, true);
        });
        assertEquals("Number of row is out of array.", exception1.getMessage());


        //incorrect inputs for column
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.set(testNumRows - 1, -1, true);
        });
        assertEquals("Number of column is out of array.", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.set(testNumRows - 1, testNumRows * testNumCols, true);
        });
        assertEquals("Number of column is out of array.", exception3.getMessage());
    }
}