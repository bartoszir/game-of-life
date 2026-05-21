package org.example;

import java.util.Random;

public class GameOfLifeBoard {

    private GameOfLifeCell[][] board; // = new boolean[height][width];
    private GameOfLifeSimulator gameSimulator;
    private int numRows; //later it should be up to the user how many Rows and Columns there will be
    private int numCols;
    private int numberOfLiveCells = 15;


    public GameOfLifeBoard(int numRows, int numCols, int numberOfLifeCells,
                           GameOfLifeSimulator gameSimulator) {

        if (numRows < 1) {
            throw new IllegalArgumentException("Number of rows must be greater than 0");
        }
        this.numRows = numRows;

        if (numCols < 1) {
            throw new IllegalArgumentException("Number of columns must be greater than 0");
        }
        this.numCols = numCols;

        if (numberOfLifeCells < 1) {
            throw new IllegalArgumentException("Number of life cells must be greater than 0");
        } else if (numberOfLifeCells > (numRows * numCols)) {
            throw new IllegalArgumentException("Number of life cells can't be be greater than number of all cells");
        }
        this.numberOfLiveCells = numberOfLifeCells;

        if (gameSimulator == null) {
            throw new IllegalArgumentException("GameOfLifeSimulator cannot be null");
        }
        this.gameSimulator = gameSimulator;

        board = new GameOfLifeCell[this.numRows][this.numCols];
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                board[r][c] = new GameOfLifeCell();
            }
        }
        setCellsNeighbours();
        setStartingBoard();
    }


    public GameOfLifeCell get(int rowNumber, int colNumber) {
        return board[rowNumber][colNumber];
    }

    public boolean getState(int rowNumber, int colNumber) {
        return get(rowNumber, colNumber).getCellValue();
    }

    /*public void setState(int rowNumber, int colNumber, boolean newState) {
        get(rowNumber, colNumber).updateState(newState);
    }*/


    public void set(int rowNumber, int colNumber, boolean newState) {
        if ((rowNumber < 0) || (rowNumber >= numRows)) {
            throw new IllegalArgumentException("Number of row is out of array.");
        }
        if ((colNumber < 0) || (colNumber >= numCols)) {
            throw new IllegalArgumentException("Number of column is out of array.");
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

            if (!get(r, c).getCellValue()) {
                get(r, c).updateState(true);
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
                GameOfLifeCell cell = get(r, c);

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

                        GameOfLifeCell neighbour = get(neighborRow, neighborCol);

                        cell.setNeighbour(cellNeighboursIndex, neighbour);
                        cellNeighboursIndex++;
                    }

                }
            }
        }
    }

    public GameOfLifeRow getRow(int row) {
        if (row < 0 || row >= numRows) {
            throw new IllegalArgumentException("Number of row is out of array.");
        }

        GameOfLifeCell[] rowCells = new GameOfLifeCell[numCols];
        for (int i = 0; i < numCols; i++) {
            rowCells[i] = board[row][i];
        }
        return new GameOfLifeRow(rowCells);
    }

    public GameOfLifeColumn getColumn(int col) {
        if (col < 0 || col >= numCols) {
            throw new IllegalArgumentException("Number of column is out of array.");
        }

        GameOfLifeCell[] columnCells = new GameOfLifeCell[numRows];
        for (int i = 0; i < numRows; i++) {
            columnCells[i] = board[i][col];
        }
        return new GameOfLifeColumn(columnCells);
    }
}
