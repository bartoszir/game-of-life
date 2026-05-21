package org.example.view;

public enum LevelSize {
    small(8),
    medium(12),
    large(24);

    private final int size;

    LevelSize(int size) {
        this.size = size;
    }
    public int getValue() {
        return size;
    }

}
