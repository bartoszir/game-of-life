package org.example;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {

    @Override
    public void doStep(GameOfLifeBoard board) {

        int height = board.getNumRows();
        int width = board.getNumCols();
        boolean[][] tmpBoard = new boolean[height][width];

        //making a copy of the current state of the gameBoard
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                tmpBoard[r][c] = board.get(r, c); // Copy its state
            }
        }

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                board.checkRules(tmpBoard, r, c);
            }
        }

        board.setGameBoard(tmpBoard);
    }
}
