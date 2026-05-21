package org.example;

import java.util.Random;

public class Main {

    private static final int ROWS = 11;
    private static final int COLS = 11;
    private static final double DENSITY = 0.3;
    private static final int ITERATIONS = 10;

    public static void main(String[] args) {
        GameOfLifeBoard board = new GameOfLifeBoard(ROWS, COLS, new PlainGameOfLifeSimulator());
        applyDensity(board, DENSITY);

        for (int i = 0; i <= ITERATIONS; i++) {
            printBoard(board, i);
            board.doSimulationStep();
        }
    }

    private static void applyDensity(GameOfLifeBoard board, double density) {
        int totalCells = (int) (board.getNumRows() * board.getNumCols() * density);
        Random random = new Random();
        for (int i = 0; i < totalCells; i++) {
            int r = random.nextInt(board.getNumRows());
            int c = random.nextInt(board.getNumCols());
            board.set(r, c, true);
        }
    }

    private static void printBoard(GameOfLifeBoard board, int iteration) {
        System.out.println("\nIteration: " + iteration);
        System.out.print("X  ");
        for (int c = 0; c < board.getNumCols(); c++) {
            System.out.print(c + "  ");
        }
        System.out.println();
        for (int r = 0; r < board.getNumRows(); r++) {
            System.out.print(r + " ");
            for (int c = 0; c < board.getNumCols(); c++) {
                System.out.print(board.get(r, c) ? " @ " : " . ");
            }
            System.out.println();
        }
        for (int j = 0; j < 1.5 * board.getNumCols(); j++) {
            System.out.print("--");
        }
        System.out.println();
    }
}
