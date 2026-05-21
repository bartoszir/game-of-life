package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameOfLifeBoardTest {

    @Test
    public void testInitialSetup() {

        GameOfLifeBoard game1 = new GameOfLifeBoard();
        GameOfLifeBoard game2 = new GameOfLifeBoard();

        boolean areBoardsDifferent = false;

        //this checks if any cell in the two game boards is different
        for (int r = 0; r < game1.getNumRows(); r++) {
            for (int c = 0; c < game1.getNumCols(); c++) {
                if (game1.getCell(r, c).isAlive() != game2.getCell(r, c).isAlive()) {
                    areBoardsDifferent = true;
                    break;
                }
            }
        }

        assertTrue(areBoardsDifferent);
    }

    @Test
    public void testCountAliveNeighbors() {

        GameOfLifeBoard game = new GameOfLifeBoard();

        int testNumRows = game.getNumRows();
        int testNumCols = game.getNumCols();

        Cell[][] testBoard = new Cell[testNumRows][testNumCols];
        for (int i = 0; i < testNumRows; i++) {
            for (int j = 0; j < testNumCols; j++) {
                testBoard[i][j] = new Cell(i, j);
            }
        }

        testBoard[5][5].setIsAlive(true); //this the cell that we are going to check after doStep()
        testBoard[4][5].setIsAlive(true);
        testBoard[4][6].setIsAlive(true);
        testBoard[5][6].setIsAlive(true);
        testBoard[6][6].setIsAlive(true);

        game.setGameBoard(testBoard);

        int count = game.countAliveNeighbours(5, 5);

        assertEquals(4, count, "the cell at [5,5] should have 4 neighbours");
    }

    // !!!!!!
    @Test
    public void testDoStep() {

        GameOfLifeBoard game = new GameOfLifeBoard();

        int testNumRows = game.getNumRows();
        int testNumCols = game.getNumCols();
        Cell[][] testBoard = new Cell[testNumRows][testNumCols];
        for (int i = 0; i < testNumRows; i++) {
            for (int j = 0; j < testNumCols; j++) {
                testBoard[i][j] = new Cell(i, j);
            }
        }

        //1st rule: dead cell with 3 alive neighbours comes to life in next iteration
        testBoard[5][5].setIsAlive(true);
        testBoard[5][4].setIsAlive(true);
        testBoard[4][5].setIsAlive(true);
        game.setGameBoard(testBoard);

        assertFalse(game.getCell(4,4).isAlive());

        game.doStep();

        assertTrue(game.getCell(4,4).isAlive());


        //2nd rule: live cell with 2 or 3 alive neighbours stays alive
        game.doStep();
        assertTrue(game.getCell(4,4).isAlive());

        testBoard[5][5].setIsAlive(false);
        game.doStep();
        assertTrue(game.getCell(4,4).isAlive());


        //reset of testBoard
        for (int i = 0; i < testNumRows; i++) {
            for (int j = 0; j < testNumCols; j++) {
                if (!testBoard[i][j].isAlive()) {
                    continue;
                }
                testBoard[i][j].setIsAlive(false);
            }
        }


        //3rd rule: live cell with 1 alive neighbour dies
        testBoard[5][5].setIsAlive(true);
        testBoard[4][5].setIsAlive(true);

        game.setGameBoard(testBoard);
        game.doStep();

        assertFalse(game.getCell(4,5).isAlive());
        assertFalse(game.getCell(5,5).isAlive());


        //4th rule: live cell with more than 3 alive neighbours dies
        testBoard[5][5].setIsAlive(true); //this the cell that we are going to check after doStep()
        testBoard[4][5].setIsAlive(true);
        testBoard[4][6].setIsAlive(true);
        testBoard[5][6].setIsAlive(true);
        testBoard[6][6].setIsAlive(true);

        game.setGameBoard(testBoard);
        game.doStep();

        assertFalse(game.getCell(5,5).isAlive());

        //reset of testBoard
        for (int i = 0; i < testNumRows; i++) {
            for (int j = 0; j < testNumCols; j++) {
                if (!testBoard[i][j].isAlive()) {
                    continue;
                }
                testBoard[i][j].setIsAlive(false);
            }
        }

        //----------------
        //checking how the algorithm behaves when the cell is on the banks
        testBoard[3][0].setIsAlive(true); //this cell has neighbours on the other side of the board
        testBoard[3][testNumCols-1].setIsAlive(true);
        testBoard[4][testNumCols-1].setIsAlive(true);

        game.setGameBoard(testBoard);
        game.doStep();

        assertTrue(game.getCell(3,0).isAlive());

        //now we will see how the algorithm behaves when the only cell that lives on our board will be the cell [3;0]
        testBoard[3][testNumCols-1].setIsAlive(false);
        testBoard[4][testNumCols-1].setIsAlive(false);

        game.setGameBoard(testBoard);
        game.doStep();

        assertFalse(game.getCell(3,0).isAlive());

        //checking how the algorithm behaves when the cell is in the corner
        testBoard[testNumRows-1][testNumCols-1].setIsAlive(true); //cell is located in the bottom's right corner
        testBoard[testNumRows-2][testNumCols-2].setIsAlive(true); //our cells' left top corner neighbour
        testBoard[0][testNumCols-2].setIsAlive(true); //our cells' left bottom corner neighbour

        game.setGameBoard(testBoard);
        game.doStep();

        assertTrue(game.getCell(testNumRows-1, testNumCols-1).isAlive());

    }

    @Test
    public void testSimulateLife() {

    }
}