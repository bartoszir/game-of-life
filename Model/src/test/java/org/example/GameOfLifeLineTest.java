package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeLineTest {
    private GameOfLifeCell liveCell;
    private GameOfLifeCell deadCell;
    private List<GameOfLifeCell> cellsList;
    private GameOfLifeLine gameOfLifeLine;

    List<GameOfLifeCell> cellsListSecond;
    GameOfLifeLine gameOfLifeLineSecond;

    // Concrete subclass to instantiate abstract class for testing
    private static class GameOfLifeLineImpl extends GameOfLifeLine {
        public GameOfLifeLineImpl(List<GameOfLifeCell> cellsList) {
            super(cellsList);
        }
    }

    @BeforeEach
    public void setUp() {
        // Initialize cells: 2 alive, 3 dead for testing
        liveCell = new GameOfLifeCell();
        liveCell.updateState(true); // Make the cell alive

        deadCell = new GameOfLifeCell(); // Dead by default

        cellsList = new ArrayList<>(Arrays.asList(liveCell, deadCell, liveCell, deadCell, deadCell));
        gameOfLifeLine = new GameOfLifeLineImpl(cellsList);

        cellsListSecond = new ArrayList<>(Arrays.asList(liveCell, deadCell, liveCell, deadCell, deadCell));
        gameOfLifeLineSecond = new GameOfLifeLineImpl(cellsListSecond);
    }

    @Test
    public void testConstructorWithAllDeadCells() {
        List<GameOfLifeCell> allDeadCells = new ArrayList<>(Arrays.asList(new GameOfLifeCell(), new GameOfLifeCell(), new GameOfLifeCell()));
        GameOfLifeLine line = new GameOfLifeLineImpl(allDeadCells);

        assertEquals(0, line.countAliveCells(), "The alive cell count should be 0.");
        assertEquals(3, line.countDeadCells(), "The dead cell count should be 3.");
    }

    @Test
    public void testConstructorWithAllAliveCells() {
        List<GameOfLifeCell> allAliveCells = new ArrayList<>(Arrays.asList(new GameOfLifeCell(), new GameOfLifeCell(), new GameOfLifeCell()));
        for (GameOfLifeCell cell : allAliveCells) {
            cell.updateState(true); // Set all cells to alive
        }
        GameOfLifeLine line = new GameOfLifeLineImpl(allAliveCells);

        assertEquals(3, line.countAliveCells(), "The alive cell count should be 3.");
        assertEquals(0, line.countDeadCells(), "The dead cell count should be 0.");
    }

    @Test
    public void testConstructorWithNullCells() {
        List<GameOfLifeCell> cellsWithNulls = new ArrayList<>(Arrays.asList(new GameOfLifeCell(), null, new GameOfLifeCell()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GameOfLifeLineImpl(cellsWithNulls);
        });

        assertEquals("The cell cannot be NULL.", exception.getMessage());
    }

    @Test
    public void testInitializationCounts() {
        assertEquals(2, gameOfLifeLine.countAliveCells(), "The initial alive cell count should be 2.");
        assertEquals(3, gameOfLifeLine.countDeadCells(), "The initial dead cell count should be 3.");
    }

    @Test
    public void testCountAliveCells() {
        assertEquals(2, gameOfLifeLine.countAliveCells(), "The alive cell count should be 2.");
    }

    @Test
    public void testCountDeadCells() {
        assertEquals(3, gameOfLifeLine.countDeadCells(), "The dead cell count should be 3.");
    }

    /*@Test
    public void testSetCellValidIndex() {
        GameOfLifeCell newCell = new GameOfLifeCell();
        newCell.updateState(false);
        gameOfLifeLine.getCell(1).updateState(true);
        int previousCountOfLiveCells = gameOfLifeLine.countAliveCells();

        gameOfLifeLine.setCell(1, newCell);
        assertSame(newCell, gameOfLifeLine.getCell(1), "The cell at index 1 should be replaced by newCell.");

        gameOfLifeLine.getCell(1).updateState(true);
        assertEquals(previousCountOfLiveCells, gameOfLifeLine.countAliveCells());

        gameOfLifeLine.getCell(1).updateState(false);
        assertEquals(previousCountOfLiveCells - 1, gameOfLifeLine.countAliveCells());
    }*/

    @Test
    public void testSetCellAtValidIndexWithDeadCell() {
        // Count before replacing
        int initialLiveCount = gameOfLifeLine.countAliveCells();
        int initialDeadCount = gameOfLifeLine.countDeadCells();

        // Create a new dead cell and set it at index 1
        GameOfLifeCell newDeadCell = new GameOfLifeCell(); // Dead by default
        gameOfLifeLine.setCell(1, newDeadCell);

        // Check if the cell is replaced correctly
        assertSame(newDeadCell, gameOfLifeLine.getCell(1));

        // Verify counts remain the same (as we're replacing a dead cell with another dead cell)
        assertEquals(initialLiveCount, gameOfLifeLine.countAliveCells());
        assertEquals(initialDeadCount, gameOfLifeLine.countDeadCells());
    }

    @Test
    public void testSetCellAtValidIndexWithLiveCell() {
        // Count before replacing
        int initialLiveCount = gameOfLifeLine.countAliveCells();
        int initialDeadCount = gameOfLifeLine.countDeadCells();

        // Create a new live cell and set it at index 1 (which was dead)
        GameOfLifeCell newLiveCell = new GameOfLifeCell();
        newLiveCell.updateState(true); // Make the cell alive
        gameOfLifeLine.setCell(1, newLiveCell);

        // Check if the cell is replaced correctly
        assertSame(newLiveCell, gameOfLifeLine.getCell(1));

        // Verify counts: live count should increase by 1 and dead count should decrease by 1
        assertEquals(initialLiveCount + 1, gameOfLifeLine.countAliveCells());
        assertEquals(initialDeadCount - 1, gameOfLifeLine.countDeadCells());
    }

    @Test
    public void testReplaceLiveCellWithDeadCell() {
        // Initial counts
        int initialLiveCount = gameOfLifeLine.countAliveCells();
        int initialDeadCount = gameOfLifeLine.countDeadCells();

        // Create a new dead cell and set it at index 0 (which was alive)
        GameOfLifeCell newDeadCell = new GameOfLifeCell(); // Dead by default
        gameOfLifeLine.setCell(0, newDeadCell);

        // Verify the cell is replaced
        assertSame(newDeadCell, gameOfLifeLine.getCell(0));

        // Verify counts: live count should decrease by 1 and dead count should increase by 1
        assertEquals(initialLiveCount - 1, gameOfLifeLine.countAliveCells());
        assertEquals(initialDeadCount + 1, gameOfLifeLine.countDeadCells());
    }

    @Test
    public void testReplaceDeadCellWithLiveCell() {
        // Initial counts
        int initialLiveCount = gameOfLifeLine.countAliveCells();
        int initialDeadCount = gameOfLifeLine.countDeadCells();

        // Create a new live cell and set it at index 1 (which was dead)
        GameOfLifeCell newLiveCell = new GameOfLifeCell();
        newLiveCell.updateState(true); // Make it alive
        gameOfLifeLine.setCell(1, newLiveCell);

        // Verify the cell is replaced
        assertSame(newLiveCell, gameOfLifeLine.getCell(1));

        // Verify counts: live count should increase by 1 and dead count should decrease by 1
        assertEquals(initialLiveCount + 1, gameOfLifeLine.countAliveCells());
        assertEquals(initialDeadCount - 1, gameOfLifeLine.countDeadCells());
    }

    @Test
    public void testSetCellInvalidIndex() {
        GameOfLifeCell newCell = new GameOfLifeCell();
        assertThrows(IllegalArgumentException.class, () -> gameOfLifeLine.setCell(-1, newCell),
                "Setting a cell at an invalid index should throw an exception.");
        assertThrows(IllegalArgumentException.class, () -> gameOfLifeLine.setCell(5, newCell),
                "Setting a cell at an invalid index should throw an exception.");

    }

    @Test
    public void testGetCellValueAtIndex() {
        assertTrue(gameOfLifeLine.get(0), "The cell at index 0 should be alive.");
        assertFalse(gameOfLifeLine.get(1), "The cell at index 1 should be dead.");
    }

    @Test
    public void testPropertyChangeFromAliveToDead() {
        // Create an event to simulate a live cell turning dead
        PropertyChangeEvent event = new PropertyChangeEvent(liveCell, "cellValue", true, false);
        gameOfLifeLine.propertyChange(event);

        assertEquals(1, gameOfLifeLine.countAliveCells(), "The alive cell count should decrease to 1.");
        assertEquals(4, gameOfLifeLine.countDeadCells(), "The dead cell count should increase to 4.");
    }

    @Test
    public void testPropertyChangeFromDeadToAlive() {
        // Create an event to simulate a dead cell turning alive
        PropertyChangeEvent event = new PropertyChangeEvent(deadCell, "cellValue", false, true);
        gameOfLifeLine.propertyChange(event);

        assertEquals(3, gameOfLifeLine.countAliveCells(), "The alive cell count should increase to 3.");
        assertEquals(2, gameOfLifeLine.countDeadCells(), "The dead cell count should decrease to 2.");
    }

    @Test
    public void testPropertyChangeWithInvalidInput() {
        // when it's not a correct message of the event
        PropertyChangeEvent event = new PropertyChangeEvent(deadCell, "wrong_message", false, true);
        gameOfLifeLine.propertyChange(event);
        assertEquals(2, gameOfLifeLine.countAliveCells(), "The alive cell count should stay at 2");
        assertEquals(3, gameOfLifeLine.countDeadCells(), "The dead cell count should stay at 3");

        // when (oldValue && !newValue) == false
        PropertyChangeEvent event2 = new PropertyChangeEvent(deadCell, "cellValue", true, true);
        gameOfLifeLine.propertyChange(event2);
        assertEquals(2, gameOfLifeLine.countAliveCells(), "The alive cell count should stay at 2");
        assertEquals(3, gameOfLifeLine.countDeadCells(), "The dead cell count should stay at 3");

        // when (!oldValue && newValue) == false
        PropertyChangeEvent event3 = new PropertyChangeEvent(deadCell, "cellValue", false, false);
        gameOfLifeLine.propertyChange(event3);
        assertEquals(2, gameOfLifeLine.countAliveCells(), "The alive cell count should stay at 2");
        assertEquals(3, gameOfLifeLine.countDeadCells(), "The dead cell count should stay at 3");

    }

    @Test
    public void toStringTest() {
        assertEquals(gameOfLifeLine.toString(), gameOfLifeLineSecond.toString());
    }

    @Test
    public void equalsTest() {
        assertTrue(gameOfLifeLine.equals(gameOfLifeLine));
        assertFalse(gameOfLifeLine.equals(null));
        assertFalse(gameOfLifeLine.equals(liveCell));

        assertTrue(gameOfLifeLine.equals(gameOfLifeLineSecond));

        assertEquals(gameOfLifeLine.hashCode(), gameOfLifeLineSecond.hashCode());

        GameOfLifeCell newLiveCell = new GameOfLifeCell();
        newLiveCell.updateState(true);
        gameOfLifeLineSecond.setCell(1, newLiveCell);

        assertFalse(gameOfLifeLine.equals(gameOfLifeLineSecond));

        assertNotEquals(gameOfLifeLine.hashCode(), gameOfLifeLineSecond.hashCode());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(gameOfLifeLine.hashCode(), gameOfLifeLineSecond.hashCode());
    }

    @Test
    public void cloneTest() {
        try {
            GameOfLifeLine clone = gameOfLifeLine.clone();
            assertNotSame(gameOfLifeLine, clone);
            assertEquals(gameOfLifeLine.cellsList, clone.cellsList);

            PropertyChangeEvent event = new PropertyChangeEvent(liveCell, "cellValue", true, false);
            clone.propertyChange(event);
            assertEquals(gameOfLifeLine.cellsList, clone.cellsList); // bo komorki w cellsList wskazuja na te same obiekty
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}