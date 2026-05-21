package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;

public class SimulationController {

    @FXML
    private GridPane gridPane;

    @FXML
    private int levelSize;

    @FXML
    private double levelDensity;

    private void renderBoard() {
        gridPane.getChildren().clear();
        Random random = new Random();
        for (int r = 0; r < levelSize; r++) {
            for (int c = 0; c < levelSize; c++) {
                Rectangle cell = new Rectangle(20, 20);
                double cellValue = random.nextDouble(1);
                cell.setFill(cellValue > levelDensity ? Color.BLACK : Color.WHITE);
                gridPane.add(cell, r, c);
            }
        }
    }

    public void setup(int size, double sensity) {
        levelSize = size;
        levelDensity = sensity;
        renderBoard();
    }

}
