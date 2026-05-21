package org.example;

import java.io.Serializable;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator, Serializable {

    @Override
    public void doStep(GameOfLifeBoard board) {

        int height = board.getNumRows();
        int width = board.getNumCols();
        boolean[][] nextStepBoard = new boolean[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                // tmpBoard[r][c] = board.get(r, c); // Copy its state
                nextStepBoard[r][c] = board.getCell(r, c).nextState();
            }
        }

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                // board.checkRules(tmpBoard, r, c);
                board.getCell(r, c).updateState(nextStepBoard[r][c]);
            }
        }

    }
}
