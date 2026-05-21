package org.example;

public class Cell {
    private final int row;
    private final int column;
    private boolean isAlive;

    public Cell(int r, int c) {
        this.row = r;
        this.column = c;
        this.isAlive = false;
    }

    public void setIsAlive(boolean alive) {
        this.isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }



    public void changeState(int neighboursCount) {
        if (isAlive()) {

            if (neighboursCount < 2) {
                setIsAlive(false);
            } else if (neighboursCount > 3) {
                setIsAlive(false);
            }

        } else {

            if (neighboursCount == 3) {
                setIsAlive(true);
            }
        }
    }
}
