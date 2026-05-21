package org.example.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class GameOfLifeApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // setting language
//        Locale.setDefault(new Locale("pl", "PL"));
        Locale.setDefault(new Locale("en", "US"));
        ResourceBundle resources = ResourceBundle.getBundle("Messages", Locale.getDefault());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("InitialConfigScene.fxml"));
        Scene scene = new Scene(loader.load());

        InitialConfigController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setTitle("GOL - Configuration");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
