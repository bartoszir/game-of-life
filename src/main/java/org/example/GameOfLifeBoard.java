package org.example;

import java.util.Random;
import java.util.Scanner;

public class GameOfLifeBoard {

    private Cell[][] gameBoard;
    private int numRows = 11; //later it should be up to the user how many Rows and Columns there will be
    private int numCols = numRows;
    private int numberOfLiveCells = 15;
    private int numberOfIterations = 10;
    private int iterationCount = 0;

    Random random = new Random();

    GameOfLifeBoard() {

        // userInput();

        gameBoard = new Cell[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                gameBoard[r][c] = new Cell(r, c);
            }
        }

        setStartingBoard();
        simulateLife();
    }

    public static void main(String[] args) throws Exception {
        GameOfLifeBoard gameOfLifeBoard = new GameOfLifeBoard();
    }

    public Cell getCell(int r, int c) {
        return gameBoard[r][c];
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public void setGameBoard(Cell[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    void setStartingBoard() {

        int liveCellsLeftCount = numberOfLiveCells;

        //random placement of live cells
        while (liveCellsLeftCount > 0) {
            int r = random.nextInt(numRows); //0-7
            int c = random.nextInt(numCols);

            Cell cell = gameBoard[r][c];
            if (!cell.isAlive()) {
                cell.setIsAlive(true);
                liveCellsLeftCount--;
            }
        }
    }

    public void displayGameBoard() {
        System.out.println("\nIteration: " + iterationCount);
        System.out.print("X  ");
        for (int i = 0; i < numCols; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        for (int r = 0; r < numRows; r++) {
            System.out.print(r + " ");
            for (int c = 0; c < numCols; c++) {
                Cell cell = gameBoard[r][c];
                if (cell.isAlive()) {
                    System.out.print(" @ ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
        for (int j = 0; j < 1.5 * numCols; j++) {
            System.out.print("--");
        }
        System.out.println();
        iterationCount++;
    }

    void simulateLife() {

        boolean isThereAnyLiveCellLeft = false;
        displayGameBoard();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (gameBoard[i][j].isAlive()) {
                    isThereAnyLiveCellLeft = true;
                    break;
                }
            }
        }

        if (!isThereAnyLiveCellLeft || iterationCount > 10) {
            return;
        }

        doStep();
        simulateLife();
    }

    //in "WIKAMP" it says that 'doStep()' generates distribution on the gameBoard
    void doStep() {

        Cell[][] tmpBoard = new Cell[numRows][numCols];

        //making a copy of the current state of the gameBoard
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                // tmpBoard[r][c] = gameBoard[r][c].clone(); <-- to mi rozwala dzialanie algorytmu!!!
                tmpBoard[r][c] = new Cell(r, c); // Create a new Cell
                tmpBoard[r][c].setIsAlive(gameBoard[r][c].isAlive()); // Copy its state
            }
        }

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                //int numberOfNeighbours = countAliveNeighbours(r, c);
                int numberOfNeighbours = countAliveNeighbours(r, c);
                tmpBoard[r][c].changeState(numberOfNeighbours);
            }
        }

        setGameBoard(tmpBoard);

    }

    public int countAliveNeighbours(int r, int c) {
        int countOfLiveCellsAround = 0;

        //loop through the neighbors (-1 to +1 in both directions)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                //skip the current cell itself
                if (i == 0 && j == 0) {
                    continue;
                }

                //get the neighbor's row and column using modular arithmetic to wrap the grid
                int neighborRow = (r + i + numRows) % numRows;
                int neighborCol = (c + j + numCols) % numCols;

                //check if the neighbor is alive
                if (gameBoard[neighborRow][neighborCol].isAlive()) {
                    countOfLiveCellsAround++;
                }
            }
        }

        return countOfLiveCellsAround;
    }


}
