package org.example;

import org.example.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeBoardTest {
    int testNumRows = 11;
    int testNumCols = 11;
    // int testNumberOfMaxIterations = 3;
//    int testNumberOfLiveCells = 15;
    String testName = "gameBoard";
    GameOfLifeSimulator testGameSimulator;
    GameOfLifeBoard testGame;
    GameOfLifeCell[][] testBoard;
    List<GameOfLifeCell> testListCellsNeighbours;
    ArrayList<GameOfLifeCell> testListCells;

    @BeforeEach
    public void setUp() {
        Locale.setDefault(new Locale("en", "EN"));
        testGameSimulator = new PlainGameOfLifeSimulator();
        testGame = new GameOfLifeBoard(testNumRows, testNumCols, testGameSimulator);

        testBoard = new GameOfLifeCell[testNumRows][testNumCols];
        testListCellsNeighbours = Arrays.asList(new GameOfLifeCell[8]);
        for (int i = 0; i < testNumRows; i++) {
            for (int j = 0; j < testNumCols; j++) {
                testBoard[i][j] = new GameOfLifeCell();
            }
        }
    }


    @Test
    public void testConstructorValidInputs() {

        assertEquals(testNumRows, testGame.getNumRows());
        assertEquals(testNumCols, testGame.getNumCols());
    }

    @Test
    public void testInitialSetup() {

        GameOfLifeBoard testGame2 = new GameOfLifeBoard(testNumRows, testNumCols, testGameSimulator);

        //deepEquals
        boolean areBoardsDifferent = Objects.deepEquals(testGame, testGame2);
        assertTrue(areBoardsDifferent);
    }

    @Test
    public void testGetRow() {
        Exception exception = assertThrows(IndexOutOfArrayException.class, () -> {
            testGame.getRow(-1);
        });
        assertEquals("Number of rows is out of array.", exception.getMessage());

        Exception exception2 = assertThrows(IndexOutOfArrayException.class, () -> {
            testGame.getRow(testNumRows + 1);
        });
        assertEquals("Number of rows is out of array.", exception2.getMessage());

        GameOfLifeCell[] expectedRowCells = new GameOfLifeCell[testNumCols];
        for (int i = 0; i < testNumCols; i++) {
            expectedRowCells[i] = testGame.getCell(2, i);
        }

        GameOfLifeRow testRow = testGame.getRow(2);

        for (int col = 0; col < testNumCols; col++) {
            assertEquals(expectedRowCells[col].getCellValue(), testRow.get(col), "The cell in row 2, column " + col + " does not match.");
        }
    }

    @Test
    public void testGetColumn() {

        Exception exception = assertThrows(IndexOutOfArrayException.class, () -> {
            testGame.getColumn(-1);
        });
        assertEquals("Number of columns is out of array.", exception.getMessage());
        // assertEquals("array.illegal-argument", exception.getMessage());

        Exception exception2 = assertThrows(IndexOutOfArrayException.class, () -> {
            testGame.getColumn(testNumCols + 1);
        });
        assertEquals("Number of columns is out of array.", exception2.getMessage());
        //assertEquals("array.illegal-argument", exception2.getMessage());

        GameOfLifeCell[] expectedColCells = new GameOfLifeCell[testNumRows];
        for (int i = 0; i < testNumRows; i++) {
            expectedColCells[i] = testGame.getCell(i, 2);
        }

        GameOfLifeColumn testCol = testGame.getColumn(2);

        for (int row = 0; row < testNumRows; row++) {
            assertEquals(expectedColCells[row].getCellValue(), testCol.get(row), "The cell in row 2, column " + row + " does not match.");
        }
    }

    @Test
    public void testSetWithCorrectInput() {
        testGame.set(5, 5, true);
        assertTrue(testGame.get(5, 5));
    }

    @Test
    public void propertyChangeListener() {
        PropertyChangeListener listener = (PropertyChangeEvent evt) -> {
        };
        GameOfLifeCell cell = new GameOfLifeCell();
        cell.addCellValueListener(listener);
        cell.updateState(true);
    }


    @Test
    public void testConstructorInvalidInputs() {

        Exception exception = assertThrows(TooSmallFieldValueException.class, () -> {
            new GameOfLifeBoard(0, testNumCols, testGameSimulator);
        });
        assertEquals("Number must be greater than 0.", exception.getMessage());

        Exception exception2 = assertThrows(TooSmallFieldValueException.class, () -> {
            new GameOfLifeBoard(testNumRows, 0, testGameSimulator);
        });
        assertEquals("Number must be greater than 0.", exception2.getMessage());

//        Exception exception3 = assertThrows(TooSmallFieldValueException.class, () -> {
//            new GameOfLifeBoard(testNumRows, testNumCols, testGameSimulator);
//        });
//        assertEquals("Number of life cells must be greater than 0.", exception3.getMessage());
//
//        int testTooMuchLiveCells = testNumRows * testNumCols + 1;
//        Exception exception4 = assertThrows(BadFieldValueException.class, () -> {
//            new GameOfLifeBoard(testNumRows, testNumCols, testTooMuchLiveCells, testGameSimulator);
//        });
//        assertEquals("Number of life cells can't be greater than number of all cells.", exception4.getMessage());

        Exception exception5 = assertThrows(BadFieldValueException.class, () -> {
            new GameOfLifeBoard(testNumRows, testNumCols, null);
        });
        assertEquals("Variable cannot be null.", exception5.getMessage());
    }

    @Test
    public void testSetInvalidInputs() {
        //incorrect inputs for row
        Exception exception = assertThrows(BadFieldValueException.class, () -> {
            testGame.set(-1, testNumCols - 1, true);
        });
        assertEquals("Number of rows is out of array.", exception.getMessage());

        Exception exception1 = assertThrows(BadFieldValueException.class, () -> {
            testGame.set(testNumRows * testNumCols, testNumCols - 1, true);
        });
        assertEquals("Number of rows is out of array.", exception1.getMessage());


        //incorrect inputs for column
        Exception exception2 = assertThrows(BadFieldValueException.class, () -> {
            testGame.set(testNumRows - 1, -1, true);
        });
        assertEquals("Number of columns is out of array.", exception2.getMessage());

        Exception exception3 = assertThrows(BadFieldValueException.class, () -> {
            testGame.set(testNumRows - 1, testNumRows + 1, true);
        });
        assertEquals("Number of columns is out of array.", exception3.getMessage());

        Exception exception4 = assertThrows(BadFieldValueException.class, () -> {
            testGame.set(testNumRows - 1, testNumRows * testNumCols, true);
        });
        assertEquals("Number of columns is out of array.", exception4.getMessage());
    }

    @Test
    public void testToString() {
        int testRows = 2;
        int testCols = 2;
        GameOfLifeBoard testBoard1 = new GameOfLifeBoard(testRows, testCols, testGameSimulator);

        for (int i = 0; i < testCols; i++) {
            for (int j = 0; j < testRows; j++) {
                testBoard1.set(i, j, true);
            }
        }

        String expectedString = "[numRows=2,numCols=2,board={{true,true},{true,true}}]";
        assertEquals(expectedString, testBoard1.toString());
    }

    @Test
    public void testEquals() {

        int testRows = 3;
        int testCols = 3;
        GameOfLifeBoard testBoard1 = new GameOfLifeBoard(testRows, testCols, testGameSimulator);
        GameOfLifeBoard testBoard2 = new GameOfLifeBoard(testCols, testCols, testGameSimulator);

        assertFalse(testBoard1.equals(null));
        assertFalse(testBoard1.equals(new GameOfLifeCell()));

        assertTrue(testBoard1.equals(testBoard1));

        for (int i = 0; i < testCols; i++) {
            for (int j = 0; j < testRows; j++) {
                testBoard1.set(i, j, true);
                testBoard2.set(i, j, testBoard1.get(i, j));
            }
        }

        assertTrue(testBoard1.equals(testBoard2));
        assertEquals(testBoard1.hashCode(), testBoard2.hashCode());

        testBoard2.set(1, 1, false);
        assertFalse(testBoard1.equals(testBoard2));
        assertNotEquals(testBoard1.hashCode(), testBoard2.hashCode());
    }

    @Test
    public void cloneTest() {
        // testGame
        try {
            GameOfLifeBoard clone = testGame.clone();
            assertNotSame(testGame, clone);
            assertEquals(testGame, clone);
            assertEquals(testGame.get(4, 4), clone.get(4, 4));

            clone.set(4, 4, !clone.get(4, 4));
            assertNotEquals(testGame.get(4, 4), clone.get(4, 4));
        } catch (AssumptionException e) {
            throw new BasicRuntimeException("wrong-assumption.clone", e);
        }
    }
}