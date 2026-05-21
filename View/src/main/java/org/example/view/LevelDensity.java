package org.example.view;

public enum LevelDensity {
    small(0.1),
    medium(0.3),
    large(0.7);

    private final double density;

    LevelDensity(double density) {
        this.density = density;
    }

    public double getValue() {
        return density;
    }
}
