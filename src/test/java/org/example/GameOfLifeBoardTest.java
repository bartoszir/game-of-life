package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeBoardTest {
    int testNumRows = 11;
    int testNumCols = 11;
    // int testNumberOfMaxIterations = 3;
    int testNumberOfLiveCells = 15;
    GameOfLifeSimulator testGameSimulator;
    GameOfLifeBoard testGame;
    GameOfLifeCell[][] testBoard;

    @BeforeEach
    void setUp() {
        testGameSimulator = new PlainGameOfLifeSimulator();
        testGame = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, testGameSimulator);

        testBoard = new GameOfLifeCell[testNumRows][testNumCols];
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
    public void testRow() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getRow(-1);
        });
        assertEquals("Number of row is out of array.", exception.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getRow(testNumRows + 1);
        });
        assertEquals("Number of row is out of array.", exception2.getMessage());

        GameOfLifeCell[] testRowCells = new GameOfLifeCell[testNumRows];

        for (int i = 0; i < testNumRows; i++) {
            testRowCells[i] = testGame.get(4, i);
        }
        GameOfLifeRow testRow = new GameOfLifeRow(testRowCells);
        assertEquals(testRow.countAliveCells(), testGame.getRow(4).countAliveCells());
        assertEquals(testRow.countDeadCells(), testGame.getRow(4).countDeadCells());

        GameOfLifeCell testCell = testGame.get(4, 2);
        testRow.setCell(2, testCell);
        assertEquals(testRow.cellsArray[2].getCellValue(), testGame.get(4, 2).getCellValue());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getRow(4).setCell(-1, testCell);
        });
        assertEquals("Index is out of array.", exception3.getMessage());

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getRow(4).setCell(testNumRows, testCell);
        });
        assertEquals("Index is out of array.", exception4.getMessage());
    }

    @Test
    public void testColumn() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getColumn(-1);
        });
        assertEquals("Number of column is out of array.", exception.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getColumn(testNumCols + 1);
        });
        assertEquals("Number of column is out of array.", exception2.getMessage());

        GameOfLifeCell[] testColsCells = new GameOfLifeCell[testNumCols];

        for (int i = 0; i < testNumCols; i++) {
            testColsCells[i] = testGame.get(i, 3);
        }
        GameOfLifeRow testColumn = new GameOfLifeRow(testColsCells);
        assertEquals(testColumn.countAliveCells(), testGame.getColumn(3).countAliveCells());
        assertEquals(testColumn.countDeadCells(), testGame.getColumn(3).countDeadCells());

        GameOfLifeCell testCell = testGame.get(1, 3);
        testColumn.setCell(1, testCell);
        assertEquals(testColumn.cellsArray[1].getCellValue(), testGame.get(1, 3).getCellValue());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getColumn(3).setCell(-1, testCell);
        });
        assertEquals("Index is out of array.", exception3.getMessage());

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.getColumn(3).setCell(testNumCols, testCell);
        });
        assertEquals("Index is out of array.", exception4.getMessage());
    }

    @Test
    public void testSetCell() {

        // Cell is Dead
        testBoard[1][0].updateState(true);
        testBoard[1][2].updateState(true);
        testGame.setGameBoard(testBoard);
        testGame.setCellsNeighbours();

        assertFalse(testGame.get(1, 1).nextState());

        testBoard[0][1].updateState(true);
        testGame.setGameBoard(testBoard);
        testGame.setCellsNeighbours();

        assertTrue(testGame.get(1, 1).nextState());

        // Cell is Alive
        testBoard[1][1].updateState(true);

        testBoard[0][1].updateState(false);
        testBoard[1][0].updateState(false);
        testGame.setGameBoard(testBoard);
        testGame.setCellsNeighbours();

        assertFalse(testGame.get(1, 1).nextState());

        testBoard[0][1].updateState(true);
        testBoard[1][0].updateState(true);
        testBoard[1][2].updateState(true);
        testBoard[2][2].updateState(true);
        testGame.setGameBoard(testBoard);
        testGame.setCellsNeighbours();

        assertFalse(testGame.get(1, 1).nextState());

        testBoard[1][2].updateState(false);
        testGame.setGameBoard(testBoard);
        testGame.setCellsNeighbours();

        assertTrue(testGame.get(1, 1).nextState());

        GameOfLifeCell testCell = testGame.get(4, 4);

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.get(1, 1).setNeighbour(-1, testCell);
        });
        assertEquals("Index is out of array.", exception3.getMessage());
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.get(1, 1).setNeighbour(9, testCell);
        });
        assertEquals("Index is out of array.", exception4.getMessage());
    }

    @Test
    public void propertyChangeListener() {
        PropertyChangeListener listener = (PropertyChangeEvent evt) -> System.out.println(evt);
        GameOfLifeCell cell = new GameOfLifeCell();
        cell.addCellValueListener(listener);
        cell.updateState(true);
    }

    @Test
    public void testUpdateStateTriggersPropertyChange() {
        GameOfLifeCell cell = testGame.get(2, 2);
        PropertyChangeEvent[] capturedEvent = new PropertyChangeEvent[1];

        // Add a listener to capture property change events
        cell.addCellValueListener(evt -> capturedEvent[0] = evt);

        // Change state and check that property change is triggered
        testGame.get(2, 2).updateState(true);  // Initially the cell should be false
        assertNotNull(capturedEvent[0], "PropertyChangeEvent should be fired when cell state changes.");
        assertEquals(false, capturedEvent[0].getOldValue(), "Old value should be false.");
        assertEquals(true, capturedEvent[0].getNewValue(), "New value should be true.");

        // Reset captured event
        capturedEvent[0] = null;

        // Change back to false and check that property change is triggered again
        testGame.get(2, 2).updateState(false);
        assertNotNull(capturedEvent[0], "PropertyChangeEvent should be fired when cell state changes.");
        assertEquals(true, capturedEvent[0].getOldValue(), "Old value should be true.");
        assertEquals(false, capturedEvent[0].getNewValue(), "New value should be false.");
    }

    @Test
    public void testUpdateStateNoPropertyChange() {
        GameOfLifeCell cell = testGame.get(2, 2);
        PropertyChangeEvent[] capturedEvent = new PropertyChangeEvent[1];

        // Add a listener to capture property change events
        cell.addCellValueListener(evt -> capturedEvent[0] = evt);

        // Set state to true and confirm that an event is triggered
        testGame.set(2, 2, true);
        assertNotNull(capturedEvent[0], "PropertyChangeEvent should be fired when cell state changes.");

        // Reset captured event
        capturedEvent[0] = null;

        // Set the same state (true) again and confirm that no event is triggered
        testGame.set(2, 2, true);
        assertNull(capturedEvent[0], "No PropertyChangeEvent should be fired when cell state remains the same.");
    }

    @Test
    public void testCountAliveNeighbors() {

        int testNumRows = testGame.getNumRows();
        int testNumCols = testGame.getNumCols();

        testBoard[5][5].updateState(true); //this the cell that we are going to check after doStep()
        testBoard[4][5].updateState(true);
        testBoard[4][6].updateState(true);
        testBoard[5][6].updateState(true);
        testBoard[6][6].updateState(true);

        testGame.setGameBoard(testBoard);

        testGame.setCellsNeighbours();

        int count = testGame.get(5, 5).countAliveNeighbours();

        assertEquals(4, count, "the cell at [5,5] should have 4 neighbours");
    }

    @Test
    public void testPropertyChangeAliveToDead() {
        GameOfLifeCell[] cellsArray = new GameOfLifeCell[5];
        for (int i = 0; i < cellsArray.length; i++) {
            cellsArray[i] = new GameOfLifeCell();
        }

        GameOfLifeLine line = new GameOfLifeRow(cellsArray);

        // Set initial states: 3 alive, 2 dead
        cellsArray[0].updateState(true);  // alive
        cellsArray[1].updateState(true);  // alive
        cellsArray[2].updateState(false); // dead
        cellsArray[3].updateState(true);  // alive
        cellsArray[4].updateState(false); // dead

        // Simulate a property change event: cell[0] goes from alive (true) to dead (false)
        PropertyChangeEvent event = new PropertyChangeEvent(cellsArray[0], "cellValue", true, false);

        // Manually invoke the propertyChange method
        line.propertyChange(event);

        // Verify liveCount and deadCount updates
        assertEquals(2, line.countAliveCells(), "Live count should decrease by 1.");
        assertEquals(3, line.countDeadCells(), "Dead count should increase by 1.");
    }

    @Test
    public void testPropertyChangeDeadToLive() {
        GameOfLifeCell[] cellsArray = new GameOfLifeCell[5];
        for (int i = 0; i < cellsArray.length; i++) {
            cellsArray[i] = new GameOfLifeCell();
        }

        GameOfLifeLine line = new GameOfLifeRow(cellsArray);

        // Set initial states: 3 alive, 2 dead
        cellsArray[0].updateState(true);  // alive
        cellsArray[1].updateState(true);  // alive
        cellsArray[2].updateState(false); // dead
        cellsArray[3].updateState(true);  // alive
        cellsArray[4].updateState(false); // dead

        PropertyChangeEvent event = new PropertyChangeEvent(cellsArray[2], "cellValue", false, true);

        // Manually invoke the propertyChange method
        line.propertyChange(event);

        // Verify liveCount and deadCount updates
        assertEquals(4, line.countAliveCells(), "Live count should increase by 1.");
        assertEquals(1, line.countDeadCells(), "Dead count should decrease by 1.");
    }

    @Test
    public void testSetInvalidInputs() {
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
            testGame.set(testNumRows - 1, testNumRows + 1, true);
        });
        assertEquals("Number of column is out of array.", exception3.getMessage());

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            testGame.set(testNumRows - 1, testNumRows * testNumCols, true);
        });
        assertEquals("Number of column is out of array.", exception4.getMessage());
    }
}