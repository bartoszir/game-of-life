package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.GameOfLifeBoard;
import org.example.JdbcGameOfLifeBoardDao;
import org.example.PlainGameOfLifeSimulator;
import org.example.exceptions.BadFieldValueException;
import org.example.exceptions.FileOperationException;

import java.sql.*;
import java.util.*;

public class InitialConfigController {
    @FXML
    private Label setupTitle;

    @FXML
    private TextField boardWidthField;

    @FXML
    private TextField boardHeightField;

    @FXML
    private ChoiceBox<Density> densityLevelChoiceBox;

    @FXML
    private Button startSimulationButton;

    @FXML
    private Button loadFromFileButton;

    @FXML
    private Label boardWidthText;

    @FXML
    private Label boardHeightText;

    @FXML
    private Label cellsDensityText;

    @FXML
    private Button englishButton;

    @FXML
    private Button polishButton;

    @FXML
    private Button authorsButton;

    private Stage primaryStage;
    private SimulationController simulationController;
    private ResourceBundle resources;

    private static final String URL = "jdbc:postgresql://localhost:5432/game_of_life";
    private static final String USER = "nbd";
    private static final String PASSWORD = "nbdpassword";

    // -------------------------------------------------------

    @FXML
    private void startSimulation(ActionEvent event) throws FileOperationException {
        try {
            // walidacja i pobranie wartosci z pol tekstowych
            int width = Integer.parseInt(boardWidthField.getText());
            int height = Integer.parseInt(boardHeightField.getText());
            Density density = densityLevelChoiceBox.getValue();

            if (width < 4 || width > 20 || height < 4 || height > 20) {
                throw new BadFieldValueException("view.dimension-wrong-field-value", new IllegalArgumentException());
            }

            GameOfLifeBoard gameOfLifeBoard = new GameOfLifeBoard(width, height, new PlainGameOfLifeSimulator());
            density.applyDensity(gameOfLifeBoard);

            FXMLLoader loader = new FXMLLoader(InitialConfigController.class.getResource("SimulationScene.fxml"));
            Scene simulationScene = new Scene(loader.load(), 600, 450);

            SimulationController simulationController = loader.getController();
            simulationController.postInitialize(gameOfLifeBoard);

            primaryStage.setScene(simulationScene);
            primaryStage.setTitle("GOL - Simulation");

        } catch (Exception e) {
            // wyswietli komunikat o bledzie (w przyszlosci moze to byc alert)
            //System.err.println(e.getMessage());
            //e.printStackTrace();
            throw new FileOperationException("view.dimension-wrong-field-value", e);
        }
    }

    @FXML
    private void loadFromFileButtonClicked(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Load Game Of Life Board");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Of Life Files", "*.gol"));
//
//        File file = fileChooser.showOpenDialog(primaryStage);
//        if (file != null) {
//            try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath())) {
//                GameOfLifeBoard gameOfLifeBoard = dao.read();
//
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("SimulationScene.fxml"));
//                Scene simulationScene = new Scene(loader.load(), 600, 450);
//
//                SimulationController simulationController1 = loader.getController();
//                simulationController1.postInitialize(gameOfLifeBoard);
//
//                primaryStage.setScene(simulationScene);
//                primaryStage.setTitle("GOL - Simulation");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        Dialog<String> dialog = new ChoiceDialog<>("Select Board", getAvailableBoardNamesFromDatabase());
        dialog.setTitle("Load Game Of Life Board");
        dialog.setHeaderText("Select a board to load:");
        dialog.setContentText("Available boards:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(boardName -> {
            try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao(boardName)) {
                GameOfLifeBoard gameOfLifeBoard = dao.read();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("SimulationScene.fxml"));
                Scene simulationScene = new Scene(loader.load(), 600, 450);

                SimulationController simulationController = loader.getController();
                simulationController.postInitialize(gameOfLifeBoard);

                primaryStage.setScene(simulationScene);
                primaryStage.setTitle("GOL - Simulation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void changeLanguageToEnglish(ActionEvent event) {
        Locale.setDefault(new Locale("en", "US"));
        reloadScene();
    }

    @FXML
    private void changeLanguageToPolish(ActionEvent event) {
        Locale.setDefault(new Locale("pl", "PL"));
        reloadScene();
    }

    @FXML
    private void showAuthors(ActionEvent event) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("org.example.view.AuthorsBundle");

            String authors = resourceBundle.getString("authors");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("authorsButton"));
            alert.setHeaderText(null);
            alert.setContentText(authors);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------

    public void initialize() {
        // load resources
        resources = ResourceBundle.getBundle("Messages", java.util.Locale.getDefault());

        loadFromFileButton.setText(resources.getString("loadFromFileButton"));
        startSimulationButton.setText(resources.getString("startSimulationButton"));
        boardWidthText.setText(resources.getString("boardWidthText"));
        boardHeightText.setText(resources.getString("boardHeightText"));
        cellsDensityText.setText(resources.getString("cellsDensityText"));
        setupTitle.setText(resources.getString("setupTitle"));
        englishButton.setText(resources.getString("englishButton"));
        polishButton.setText(resources.getString("polishButton"));
        authorsButton.setText(resources.getString("authorsButton"));


        // fill ChoiceBox with values for the density
        densityLevelChoiceBox.getItems().setAll(Density.values());
        densityLevelChoiceBox.setValue(Density.MEDIUM);

        // Ustawienia domyślne dla pól tekstowych
        boardWidthField.setText("10");
        boardHeightField.setText("10");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void reloadScene() {
        try {
            // ladujemy ponownie tę samą scenę z nowym językiem
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InitialConfigScene.fxml"));
            ResourceBundle resources = ResourceBundle.getBundle("Messages", Locale.getDefault());
            loader.setResources(resources);
            Scene newScene = new Scene(loader.load());

            // przypisanie nowego kontrolera
            InitialConfigController newController = loader.getController();
            newController.setPrimaryStage(primaryStage);

            // ustawienie nowej sceny
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getAvailableBoardNamesFromDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM game_of_life_boards");
             ResultSet resultSet = statement.executeQuery()) {

            List<String> boardNames = new ArrayList<>();
            while (resultSet.next()) {
                boardNames.add(resultSet.getString("name"));
            }
            return boardNames;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of("Error loading boards");
        }
    }
}
