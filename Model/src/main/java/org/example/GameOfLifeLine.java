package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.AssumptionException;
import org.example.exceptions.BadFieldValueException;
import org.example.exceptions.GameOfLifeCellNullException;
import org.example.exceptions.IndexOutOfArrayException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class GameOfLifeLine implements PropertyChangeListener, Serializable, Cloneable {
    private static final Logger logger = LogManager.getLogger(GameOfLifeLine.class);
    protected List<GameOfLifeCell> cellsList;
    public int liveCount;
    public int deadCount;

    public GameOfLifeLine(List<GameOfLifeCell> cellsList) {
        this.cellsList = cellsList;
        int count = 0;
        for (GameOfLifeCell cell : cellsList) {
            if (cell == null) {
                //throw new IllegalArgumentException("The cell cannot be NULL.");
                var exception = new GameOfLifeCellNullException("class.null-reference",
                        new IllegalArgumentException());
                exception.log(logger);
                throw exception;
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
            //throw new IllegalArgumentException("Index is out of array.");
            var exception = new IndexOutOfArrayException("array.illegal-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
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

    // --------------------------------------------------------------------------------------------------------------
    // metody z cwiczenia 6

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("cellsList", getCellsValues())
                .append("liveCount", liveCount)
                .append("deadCount", deadCount)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        GameOfLifeLine other = (GameOfLifeLine) obj;
        return new EqualsBuilder()
                //.appendSuper(super.equals(obj))
                .append(getCellsValues(), other.getCellsValues())
                .append(liveCount, other.liveCount)
                .append(deadCount, other.deadCount)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 37)
                .append(getCellsValues())
                .append(liveCount)
                .append(deadCount)
                .toHashCode();
    }


    // return ArrayList of cells value (boolean), used by methods: toString, equals, hashCode

    private ArrayList<Boolean> getCellsValues() {
        ArrayList<Boolean> gameOfLifeArray = new ArrayList<>(8);
        for (GameOfLifeCell gameOfLifeCell : cellsList) {
            gameOfLifeArray.add(gameOfLifeCell.getCellValue());
        }
        return gameOfLifeArray;
    }

    @Override
    public GameOfLifeLine clone() throws AssumptionException {
        try {
            GameOfLifeLine clone = (GameOfLifeLine) super.clone();
            clone.cellsList = new ArrayList<>(this.cellsList.size());
            for (GameOfLifeCell cell : cellsList) {
                clone.cellsList.add((GameOfLifeCell) cell.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            var exception = new AssumptionException("wrong-assumption.clone", e);
            exception.log(logger);
            throw exception;
        }
    }
}
