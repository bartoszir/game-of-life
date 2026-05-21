package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class ConfigurationController {

    private Stage primaryStage;

    @FXML
    private ChoiceBox<LevelSize> levelSizeChoiceBox;

    @FXML
    private ChoiceBox<LevelDensity> levelDensityChoiceBox;

    @FXML
    public void initialize() {
        levelSizeChoiceBox.getItems().setAll(LevelSize.values());
        levelSizeChoiceBox.setValue(LevelSize.medium);

        levelDensityChoiceBox.getItems().setAll(LevelDensity.values());
        levelDensityChoiceBox.setValue(LevelDensity.medium);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void onStartSimulation(ActionEvent event) {
        try {


            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("simulationScene.fxml"));
            Scene simulationScene = new Scene(loader.load(), 600, 600);

            SimulationController simulationController = loader.getController();
            simulationController.setup(levelSizeChoiceBox.getValue().getValue(), levelDensityChoiceBox.getValue().getValue());

            primaryStage.setScene(simulationScene);
            primaryStage.setTitle("GoL Simulation");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
