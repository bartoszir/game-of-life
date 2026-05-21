package org.example;

import java.util.Random;

public class GameOfLifeBoard {

    private boolean[][] board; // = new boolean[height][width];
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

        board = new boolean[this.numRows][this.numCols];

        setStartingBoard();
        // simulateLife();
    }


    public boolean get(int rowNumber, int colNumber) {
        return board[rowNumber][colNumber];
    }


    public void set(int rowNumber, int colNumber, boolean state) {
        if ((rowNumber < 0) || (rowNumber >= numRows)) {
            throw new IllegalArgumentException("Number of row is out of array.");
        }
        if ((colNumber < 0) || (colNumber >= numCols)) {
            throw new IllegalArgumentException("Number of column is out of array.");
        }
        board[rowNumber][colNumber] = state;
    }


    public int getNumRows() {
        return numRows;
    }


    public int getNumCols() {
        return numCols;
    }

    //private void setNumRows(int numRows) {
    //    this.numRows = numRows;
    //}

    //private void setNumCols(int numCols) {
    //    this.numCols = numCols;
    //}


    public void setGameBoard(boolean[][] newBoard) {
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
                set(r, c, true);
                liveCellsLeftCount--;
            }
        }
    }


    public int countAliveNeighbours(int rowNumber, int colNumber) {
        int countOfLiveCellsAround = 0;

        //loop through the neighbors (-1 to +1 in both directions)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                //skip the current cell itself
                if (i == 0 && j == 0) {
                    continue;
                }

                //get the neighbor's row and column using modular arithmetic to wrap the grid
                int neighborRow = (rowNumber + i + numRows) % numRows;
                int neighborCol = (colNumber + j + numCols) % numCols;

                //check if the neighbor is alive
                if (board[neighborRow][neighborCol]) {
                    countOfLiveCellsAround++;
                }
            }
        }

        return countOfLiveCellsAround;
    }


    public void checkRules(boolean[][] whichBoard, int rowNumber, int colNumber) {
        int neighboursCount = countAliveNeighbours(rowNumber, colNumber);
        if (!whichBoard[rowNumber][colNumber]) {
            if (neighboursCount == 3) {
                whichBoard[rowNumber][colNumber] = true;
            }
        } else {
            if (neighboursCount < 2 || neighboursCount > 3) {
                whichBoard[rowNumber][colNumber] = false;
            }
        }
    }


    public void doSimulationStep() {
        gameSimulator.doStep(this);
    }
}
