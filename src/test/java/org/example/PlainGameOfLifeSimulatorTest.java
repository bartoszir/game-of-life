package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlainGameOfLifeSimulatorTest {

    int testNumRows = 11;
    int testNumCols = 11;
    // int testNumberOfMaxIterations = 3;
    int testNumberOfLiveCells = 15;
    GameOfLifeBoard testGameBoard;
    GameOfLifeSimulator testGameSimulator;


    @BeforeEach
    public void setUp() {
        testGameSimulator = new PlainGameOfLifeSimulator();
        testGameBoard = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, testGameSimulator);
    }

    @Test
    public void testDoStepFirstRule() {
        //1st rule: dead cell with 3 alive neighbours comes to life in next iteration


        // GameOfLifeBoard game = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, gameOfLifeSimulator);

        int testNumRows = testGameBoard.getNumRows();
        int testNumCols = testGameBoard.getNumCols();
        boolean[][] testBoard = new boolean[testNumRows][testNumCols];

        testBoard[5][5] = true;
        testBoard[5][4] = true;
        testBoard[4][5] = true;
        testGameBoard.setGameBoard(testBoard);

        assertFalse(testGameBoard.get(4, 4));

        testGameSimulator.doStep(testGameBoard);

        assertTrue(testGameBoard.get(4, 4));

    }

    @Test
    public void testDoStepSecondRule() {
        //2nd rule: live cell with 2 or 3 alive neighbours stays alive

        //GameOfLifeBoard game = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfMaxIterations, testNumberOfLiveCells, gameOfLifeSimulator);

        int testNumRows = testGameBoard.getNumRows();
        int testNumCols = testGameBoard.getNumCols();
        boolean[][] testBoard = new boolean[testNumRows][testNumCols];

        testBoard[5][5] = true;
        testBoard[5][4] = true;
        testBoard[4][5] = true;
        testGameBoard.setGameBoard(testBoard);

        testGameBoard.doSimulationStep();
        assertTrue(testGameBoard.get(4, 4));

        testBoard[5][5] = false;
        testGameSimulator.doStep(testGameBoard);
        assertTrue(testGameBoard.get(4, 4));

    }

    @Test
    public void testDoStepThirdRule() {
        //3rd rule: live cell with 1 alive neighbour dies

        // GameOfLifeBoard game = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfMaxIterations, testNumberOfLiveCells, gameOfLifeSimulator);

        int testNumRows = testGameBoard.getNumRows();
        int testNumCols = testGameBoard.getNumCols();
        boolean[][] testBoard = new boolean[testNumRows][testNumCols];

        testBoard[5][5] = true;
        testBoard[4][5] = true;

        testGameBoard.setGameBoard(testBoard);

        testGameSimulator.doStep(testGameBoard);

        assertFalse(testGameBoard.get(4, 5));
        assertFalse(testGameBoard.get(5, 5));
    }

    @Test
    public void testDoStepFourthRule() {
        //4th rule: live cell with more than 3 alive neighbours dies

        // GameOfLifeBoard game = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfMaxIterations, testNumberOfLiveCells, gameOfLifeSimulator);

        int testNumRows = testGameBoard.getNumRows();
        int testNumCols = testGameBoard.getNumCols();
        boolean[][] testBoard = new boolean[testNumRows][testNumCols];

        testBoard[5][5] = true; //this the cell that we are going to check after doStep()
        testBoard[4][5] = true;
        testBoard[4][6] = true;
        testBoard[5][6] = true;
        testBoard[6][6] = true;

        testGameBoard.setGameBoard(testBoard);

        testGameSimulator.doStep(testGameBoard);

        assertFalse(testGameBoard.get(5, 5));
    }

    @Test
    public void testDoStepCellOnFrame() {
        //checking how the algorithm behaves when the cell is on the banks

        // GameOfLifeBoard game = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfMaxIterations, testNumberOfLiveCells, gameOfLifeSimulator);

        int testNumRows = testGameBoard.getNumRows();
        int testNumCols = testGameBoard.getNumCols();
        boolean[][] testBoard = new boolean[testNumRows][testNumCols];

        testBoard[3][0] = true; //this cell has neighbours on the other side of the board
        testBoard[3][testNumCols - 1] = true;
        testBoard[4][testNumCols - 1] = true;

        testGameBoard.setGameBoard(testBoard);
        testGameSimulator.doStep(testGameBoard);

        assertTrue(testGameBoard.get(3, 0));

        //now we will see how the algorithm behaves when the only cell that lives on our board will be the cell [3;0]
        testBoard[3][testNumCols - 1] = false;
        testBoard[4][testNumCols - 1] = false;

        testGameBoard.setGameBoard(testBoard);
        testGameSimulator.doStep(testGameBoard);

        assertFalse(testGameBoard.get(3, 0));

        //checking how the algorithm behaves when the cell is in the corner
        testBoard[testNumRows - 1][testNumCols - 1] = true; //cell is located in the bottom's right corner
        testBoard[testNumRows - 2][testNumCols - 2] = true; //our cells' left top corner neighbour
        testBoard[0][testNumCols - 1] = true; //our cells' left bottom corner neighbour

        testGameBoard.setGameBoard(testBoard);
        testGameSimulator.doStep(testGameBoard);

        assertTrue(testGameBoard.get(testNumRows - 1, testNumCols - 1));

    }
}