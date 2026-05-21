package org.example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class GameOfLifeLine implements PropertyChangeListener {
    protected GameOfLifeCell[] cellsArray;
    private int liveCount;
    private int deadCount;

    public GameOfLifeLine(GameOfLifeCell[] cellsArray) {
        this.cellsArray = cellsArray;
        int count = 0;
        for (GameOfLifeCell cell : cellsArray) {
            if (cell != null && cell.getCellValue()) {
                count++;
            }
        }
        this.liveCount = count;
        this.deadCount = cellsArray.length - liveCount;
        for (GameOfLifeCell cell : cellsArray) {
            cell.addCellValueListener(this); // Register each cell as a listener
        }

    }

    public int countAliveCells() {
        return liveCount;
    }

    public int countDeadCells() {
        return deadCount;
    }

    public void setCell(int index, GameOfLifeCell cell) {
        if (index >= 0 && index < cellsArray.length) {
            cellsArray[index] = cell;
        } else {
            throw new IllegalArgumentException("Index is out of array.");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("cellValue".equals(event.getPropertyName())) {
            boolean oldValue = (boolean) event.getOldValue();
            boolean newValue = (boolean) event.getNewValue();

            if (oldValue && !newValue) {
                // Cell went from live to dead
                liveCount--;
                deadCount++;
            } else if (!oldValue && newValue) {
                // Cell went from dead to live
                liveCount++;
                deadCount--;
            }
        }
    }
}
