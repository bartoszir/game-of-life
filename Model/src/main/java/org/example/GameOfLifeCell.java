package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameOfLifeCell implements Serializable {
    private boolean value;
    // private GameOfLifeCell[] neighbours;
    private List<GameOfLifeCell> neighbours;
    private PropertyChangeSupport support;

    public GameOfLifeCell() {
        this.value = false;
        // this.neighbours = new GameOfLifeCell[8];
        this.neighbours = Arrays.asList(new GameOfLifeCell[8]);
        this.support = new PropertyChangeSupport(this);
    }

    public void setNeighbour(int index, GameOfLifeCell neighbour) {
        if (index >= 0 && index <= 7) {
            this.neighbours.set(index, neighbour);
        } else {
            throw new IllegalArgumentException("Index is out of array.");
        }
    }

    public GameOfLifeCell getNeighbour(int index) {
        return this.neighbours.get(index);
    }

    public boolean getCellValue() {
        return this.value;
    }

    public void setCellValue(boolean value) {
        this.value = value;
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
        setCellValue(newState);

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

    public void removeCellValueListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

    // --------------------------------------------------------------------------------------------------------------
    // metody z cwiczenia 6

    /**
     * this produces a toString of the format [value=true, neighbours=[....]]
     *
     * @return a string containing values of the attributes of the objects.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("value", value)
                .append("neighbours", getNeighboursCellValues())
                .toString();
    }

    /**
     * checks if objects are the same.
     *
     * <p>This method checks if the current object and the given object are of the same class
     * * and if their attributes (cellValue and neighbours) are the same.</p>
     *
     * @param obj an object that we want to compare.
     * @return true or false. Informing if given object is the same or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        GameOfLifeCell other = (GameOfLifeCell) obj;
        return new EqualsBuilder()
                .append(value, other.value)
                .append(getNeighboursCellValues(), other.getNeighboursCellValues())
                .isEquals();
    }

    /**
     * It's a method that generates unique hash code for the object.
     *
     * <p>If objects are equals they will have identical hash code
     * Inside we use HashCodeBuilder() with parameters 17 and 37. These are prime number which supposedly
     * increase randomness.</p>
     *
     * @return an integer
     */
    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(getNeighboursCellValues())
                .toHashCode();
    }

    private ArrayList<Boolean> getNeighboursCellValues() {
        ArrayList<Boolean> gameOfLifeArray = new ArrayList<>(8);
        for (GameOfLifeCell gameOfLifeCell : neighbours) {
            gameOfLifeArray.add(gameOfLifeCell.getCellValue());
        }
        return gameOfLifeArray;
    }
}
