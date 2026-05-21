package org.example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

public abstract class GameOfLifeLine implements PropertyChangeListener {
    // protected GameOfLifeCell[] cellsArray;
    protected List<GameOfLifeCell> cellsList = Arrays.asList(new GameOfLifeCell[8]);
    public int liveCount;
    public int deadCount;

    public GameOfLifeLine(List<GameOfLifeCell> cellsList) {
        this.cellsList = cellsList;
        int count = 0;
        for (GameOfLifeCell cell : cellsList) {
            if (cell == null) {
                throw new IllegalArgumentException("The cell cannot be NULL.");
            }
            if (cell.getCellValue()) {
                count++;
            }
        }
        this.liveCount = count;
        this.deadCount = cellsList.size() - liveCount;

        for (GameOfLifeCell cell : cellsList) {
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
        if (index >= 0 && index < cellsList.size()) {
            GameOfLifeCell oldCell = cellsList.get(index);

            // remove this listener from the old cell
            oldCell.removeCellValueListener(this);
            // adjust counts based on the old cell state
            if (oldCell.getCellValue()) {
                this.liveCount--;
                this.deadCount++;
            }

            // set the new cell and register it as a listener
            cellsList.set(index, cell);
            cellsList.get(index).addCellValueListener(this);

            if (cellsList.get(index).getCellValue()) {
                this.liveCount++;
                this.deadCount--;
            }

        } else {
            throw new IllegalArgumentException("Index is out of array.");
        }
    }

    public boolean get(int index) {
        return cellsList.get(index).getCellValue();
    }

    public GameOfLifeCell getCell(int index) {
        return cellsList.get(index);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("cellValue".equals(event.getPropertyName())) {
            boolean oldValue = (boolean) event.getOldValue();
            boolean newValue = (boolean) event.getNewValue();

            if (oldValue && !newValue) {
                // Cell went from live to dead
                this.liveCount--;
                this.deadCount++;
            } else if (!oldValue && newValue) {
                // Cell went from dead to live
                this.liveCount++;
                this.deadCount--;
            }
        }
    }
}
