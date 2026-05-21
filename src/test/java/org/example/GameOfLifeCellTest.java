package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeCellTest {
    private GameOfLifeCell cell;
    private GameOfLifeCell[] testNeighbours;

    @BeforeEach
    public void setUp() {
        cell = new GameOfLifeCell();
        testNeighbours = new GameOfLifeCell[8];
        for (int i = 0; i < testNeighbours.length; i++) {
            testNeighbours[i] = new GameOfLifeCell();
        }
    }

    @Test
    public void testSetNeighbourValidIndex() {
        GameOfLifeCell testNeighbour = new GameOfLifeCell();

        cell.setNeighbour(0, testNeighbour);
        assertSame(testNeighbour, cell.getNeighbour(0));

        cell.setNeighbour(7, testNeighbour);
        assertSame(testNeighbour, cell.getNeighbour(7));
    }

    @Test
    public void testSetNeighbourInvalidIndex() {
        GameOfLifeCell testNeighbour = new GameOfLifeCell();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cell.setNeighbour(-1, testNeighbour);
        });
        assertEquals("Index is out of array.", exception.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            cell.setNeighbour(8, testNeighbour);
        });

        assertEquals("Index is out of array.", exception2.getMessage());
    }

    @Test
    public void getCellValue() {
        assertFalse(cell.getCellValue());
    }

    @Test
    public void nextStateReproduction() {
        testNeighbours[0].updateState(true);
        testNeighbours[1].updateState(true);
        testNeighbours[2].updateState(true);

        for (int i = 0; i < 3; i++) {
            cell.setNeighbour(i, testNeighbours[i]);
        }

        assertTrue(cell.nextState());
    }

    @Test
    public void nextStateUnderpopulation() {
        cell.updateState(true);
        cell.setNeighbour(0, testNeighbours[0]);
        assertFalse(cell.nextState());
    }

    @Test
    public void nextStateOverpopulation() {
        cell.updateState(true);
        for (int i = 0; i < 4; i++) {
            testNeighbours[i].updateState(true);
            cell.setNeighbour(i, testNeighbours[i]);
        }
        assertFalse(cell.nextState());
    }

    @Test
    public void nextStateSurvival() {
        cell.updateState(true);
        testNeighbours[0].updateState(true);
        testNeighbours[1].updateState(true);
        cell.setNeighbour(0, testNeighbours[0]);
        cell.setNeighbour(1, testNeighbours[1]);
        assertTrue(cell.nextState());
    }

    @Test
    public void updateStateTriggersPropertyChange() {
        PropertyChangeListener listener = (PropertyChangeEvent evt) -> {
            assertEquals("cellValue", evt.getPropertyName());
            assertEquals(false, evt.getOldValue());
            assertEquals(true, evt.getNewValue());
        };

        GameOfLifeCell cell = new GameOfLifeCell();
        cell.addCellValueListener(listener);

        cell.updateState(true);

        assertTrue(cell.getCellValue());
    }

    @Test
    public void updateStateNoPropertyChangeIfSameState() {
        PropertyChangeListener listener = evt -> fail("Listener should not be called");
        cell.addCellValueListener(listener);
        cell.updateState(false);  // No change in value, so listener should not be called
    }

    @Test
    public void countAliveNeighbours() {
        testNeighbours[0].updateState(true);
        testNeighbours[1].updateState(true);
        testNeighbours[2].updateState(true);

        for (int i = 0; i < 3; i++) {
            cell.setNeighbour(i, testNeighbours[i]);
        }

        assertEquals(3, cell.countAliveNeighbours());
    }

    @Test
    public void addAndRemoveCellValueListener() {
        PropertyChangeListener listener = evt -> fail("Listener should not be called after removal");

        cell.addCellValueListener(listener);
        cell.removeCellValueListener(listener);

        cell.updateState(true);  // Listener should not be triggered after removal
    }


}