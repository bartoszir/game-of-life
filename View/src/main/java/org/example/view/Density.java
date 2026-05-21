package org.example.view;

import org.example.GameOfLifeBoard;

public enum Density {
    SMALL(0.1),
    MEDIUM(0.3),
    LARGE(0.5);

    private final double density;

    Density(double density) {
        this.density = density;
    }

    public double getDensity() {
        return density;
    }

    public void applyDensity(GameOfLifeBoard gameBoard) {
        int rows = gameBoard.getNumRows();
        int cols = gameBoard.getNumCols();
        int totalCells = rows * cols;
        int cellsToFill = (int) (totalCells * density);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gameBoard.set(i, j, false);
            }
        }

        for (int i = 0; i < cellsToFill; i++) {
            int r = (int) (Math.random() * rows);
            int c = (int) (Math.random() * cols);
            gameBoard.set(r, c, true);
        }
    }
}
