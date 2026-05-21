package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameOfLifeCell {
    private boolean value;
    private GameOfLifeCell[] neighbours;
    private PropertyChangeSupport support;

    public GameOfLifeCell() {
        this.value = false;
        this.neighbours = new GameOfLifeCell[8];
        this.support = new PropertyChangeSupport(this);
    }

    public void setNeighbour(int index, GameOfLifeCell neighbour) {
        if (index >= 0 && index <= 7) {
            this.neighbours[index] = neighbour;
        } else {
            throw new IllegalArgumentException("Index is out of array.");
        }
    }

    public boolean getCellValue() {
        return this.value;
    }

    public boolean nextState() {
        boolean newState = getCellValue();
        int aliveNeighboursCount = countAliveNeighbours();

        if (!getCellValue()) {
            if (aliveNeighboursCount == 3) {
                newState = true;
            }
        } else {
            if (aliveNeighboursCount < 2 || aliveNeighboursCount > 3) {
                newState = false;
            }
        }

        return newState;
    }

    public void updateState(boolean newState) {
        boolean oldValue = value;
        this.value = newState;

        if (oldValue != newState) {
            support.firePropertyChange("cellValue", oldValue, newState);  // Notify listeners of the change
        }
    }

    public int countAliveNeighbours() {
        int countAliveNeighbours = 0;

        for (GameOfLifeCell neighbour : neighbours) {
            if (neighbour != null && neighbour.getCellValue()) {
                countAliveNeighbours++;
            }
        }
        return countAliveNeighbours;
    }


    /**
     * Te motody muszą się nazywać addPropertyChangeListener albo addCellValueListener.
     *
     * @param listener - listener.
     */
    public void addCellValueListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    /*public void removeCellValueListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }*/
}
