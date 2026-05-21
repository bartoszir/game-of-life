package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.AssumptionException;
import org.example.exceptions.BadFieldValueException;
import org.example.exceptions.IndexOutOfArrayException;
import org.example.exceptions.TooSmallFieldValueException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameOfLifeBoard implements Serializable, Cloneable {
    private static final Logger logger = LogManager.getLogger(GameOfLifeBoard.class);
    private GameOfLifeCell[][] board; // = new boolean[height][width];
    private GameOfLifeSimulator gameSimulator;
    private int numRows; //later it should be up to the user how many Rows and Columns there will be
    private int numCols;
    private int numberOfLiveCells = 15;


    public GameOfLifeBoard(int numRows, int numCols, int numberOfLifeCells,
                           GameOfLifeSimulator gameSimulator) {

        if (numRows < 1) {
            //throw new IllegalArgumentException("Number of rows must be greater than 0");
            var exception = new TooSmallFieldValueException("array.zero-as-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }
        this.numRows = numRows;

        if (numCols < 1) {
            //throw new IllegalArgumentException("Number of columns must be greater than 0");
            var exception = new TooSmallFieldValueException("array.zero-as-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }
        this.numCols = numCols;

        if (numberOfLifeCells < 1) {
            //throw new IllegalArgumentException("Number of life cells must be greater than 0");
            var exception = new TooSmallFieldValueException("cells.negative-value",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        } else if (numberOfLifeCells > (numRows * numCols)) {
            //throw new IllegalArgumentException("Number of life cells can't be greater than number of all cells");
            var exception = new BadFieldValueException("cells.illegal-value",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }
        this.numberOfLiveCells = numberOfLifeCells;

        if (gameSimulator == null) {
            //throw new IllegalArgumentException("GameOfLifeSimulator cannot be null");
            var exception = new BadFieldValueException("class.null-reference",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }
        this.gameSimulator = gameSimulator;

        this.board = new GameOfLifeCell[this.numRows][this.numCols];
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                board[r][c] = new GameOfLifeCell();
            }
        }
        setCellsNeighbours();
        setStartingBoard();
    }


    public boolean get(int rowNumber, int colNumber) {
        return board[rowNumber][colNumber].getCellValue();
    }


    public GameOfLifeCell getCell(int rowNumber, int colNumber) {
        return board[rowNumber][colNumber];
    }


    public void set(int rowNumber, int colNumber, boolean newState) {
        if ((rowNumber < 0) || (rowNumber >= numRows)) {
            //throw new IllegalArgumentException("Number of row is out of array.");
            var exception = new IndexOutOfArrayException("array.illegal-row-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }
        if ((colNumber < 0) || (colNumber >= numCols)) {
            //throw new IllegalArgumentException("Number of column is out of array.");
            var exception = new IndexOutOfArrayException("array.illegal-column-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }
        board[rowNumber][colNumber].updateState(newState);
    }


    public int getNumRows() {
        return numRows;
    }


    public int getNumCols() {
        return numCols;
    }


    public void setGameBoard(GameOfLifeCell[][] newBoard) {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                this.board[r][c] = newBoard[r][c];
            }
        }
    }


    private void setStartingBoard() {
        Random random = new Random();
        int liveCellsLeftCount = numberOfLiveCells;

        //random placement of live cells
        while (liveCellsLeftCount > 0) {
            int r = random.nextInt(numRows); //0-7
            int c = random.nextInt(numCols);

            if (!get(r, c)) {
                getCell(r, c).updateState(true);
                liveCellsLeftCount--;
            }
        }
    }

    public void doSimulationStep() {
        gameSimulator.doStep(this);
    }

    public void setCellsNeighbours() {
        int cellNeighboursIndex;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {

                cellNeighboursIndex = 0;
                GameOfLifeCell cell = getCell(r, c);

                //it starts with the top left corner and goes clockwise
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        //skip the current cell itself
                        if (i == 0 && j == 0) {
                            continue;
                        }

                        //get the neighbor's row and column using modular arithmetic to wrap the grid
                        int neighborRow = (r + i + numRows) % numRows;
                        int neighborCol = (c + j + numCols) % numCols;

                        GameOfLifeCell neighbour = getCell(neighborRow, neighborCol);

                        cell.setNeighbour(cellNeighboursIndex, neighbour);
                        cellNeighboursIndex++;
                    }

                }
            }
        }
    }

    public GameOfLifeRow getRow(int row) {
        if (row < 0 || row >= numRows) {
            //throw new IllegalArgumentException("Number of row is out of array.");
            var exception = new IndexOutOfArrayException("array.illegal-row-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }

        // GameOfLifeCell[] rowCells = new GameOfLifeCell[numCols];
        List<GameOfLifeCell> rowCells = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            // rowCells[i] = board[row][i];
            rowCells.add(new GameOfLifeCell());
            rowCells.get(i).updateState(get(row, i));
        }
        return new GameOfLifeRow(rowCells);
    }

    public GameOfLifeColumn getColumn(int col) {
        if (col < 0 || col >= numCols) {
            //throw new IllegalArgumentException("Number of column is out of array.");
            var exception = new IndexOutOfArrayException("array.illegal-column-argument",
                    new IllegalArgumentException());
            exception.log(logger);
            throw exception;
        }

        // GameOfLifeCell[] columnCells = new GameOfLifeCell[numRows];
        List<GameOfLifeCell> columnCells = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            // columnCells[i] = board[i][col];
            columnCells.add(new GameOfLifeCell());
            columnCells.get(i).updateState(get(i, col));
        }
        return new GameOfLifeColumn(columnCells);
    }

    // --------------------------------------------------------------------------------------------------------------
    // metody z cwiczenia 6

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("numRows", numRows)
                .append("numCols", numCols)
                .append("board", getBoardCellsValue())
                .toString();
    }

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

        GameOfLifeBoard other = (GameOfLifeBoard) obj;
        return new EqualsBuilder()
                .append(numRows, other.numRows)
                .append(numCols, other.numCols)
                .append(getBoardCellsValue(), other.getBoardCellsValue())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(numRows)
                .append(numCols)
                .append(getBoardCellsValue())
                .toHashCode();
    }

    private boolean[][] getBoardCellsValue() {
        boolean[][] cellsValue = new boolean[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                cellsValue[r][c] = get(r, c);
            }
        }

        return cellsValue;
    }

    @Override
    public GameOfLifeBoard clone() throws AssumptionException {
        try {
            GameOfLifeBoard clone = (GameOfLifeBoard) super.clone();

            clone.board = new GameOfLifeCell[numRows][numCols];
            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    clone.board[r][c] = this.board[r][c].clone();
                }
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            var exception = new AssumptionException("wrong-assumption.clone", e);
            exception.log(logger);
            throw exception;
        }
    }
}
