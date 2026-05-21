package org.example.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderConfiguration = new FXMLLoader(HelloApplication.class.getResource("configurationScene.fxml"));
        Scene configurationScene = new Scene(fxmlLoaderConfiguration.load(), 600, 600);

        ConfigurationController controller = fxmlLoaderConfiguration.getController();
        controller.setPrimaryStage(stage);

        stage.setScene(configurationScene);
        stage.setTitle("GoL Configuration");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}