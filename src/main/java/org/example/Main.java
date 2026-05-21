package org.example;

public class Main {

    private static final int ROWS = 11;
    private static final int COLS = 11;
    private static final int LIVE_CELLS = 15;
    private static final int ITERATIONS = 10;

    public static void main(String[] args) {
        GameOfLifeBoard board = new GameOfLifeBoard(ROWS, COLS, LIVE_CELLS, new PlainGameOfLifeSimulator());

        for (int i = 0; i <= ITERATIONS; i++) {
            printBoard(board, i);
            board.doSimulationStep();
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
                System.out.print(board.getState(r, c) ? " @ " : " . ");
            }
            System.out.println();
        }
        for (int j = 0; j < 1.5 * board.getNumCols(); j++) {
            System.out.print("--");
        }
        System.out.println();
    }
}
