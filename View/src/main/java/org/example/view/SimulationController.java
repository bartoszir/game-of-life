package org.example.view;

import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.GameOfLifeBoard;
import org.example.GameOfLifeCell;
import org.example.JdbcGameOfLifeBoardDao;
import org.example.exceptions.FileOperationException;
import org.example.exceptions.MethodNotFoundException;

import java.util.Optional;
import java.util.ResourceBundle;

public class SimulationController {
    private static final Logger logger = LogManager.getLogger(SimulationController.class);
    @FXML
    private GridPane boardGrid;

    @FXML
    private Label iterationCountLabel;

    @FXML
    private Button nextStepButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button cleanBoardButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label gameTitle;

    private GameOfLifeBoard gameBoard;
    private int iterationCount = 0;
    private ResourceBundle resources;

    private static final String URL = "jdbc:postgresql://localhost:5432/game_of_life";
    private static final String USER = "nbd";
    private static final String PASSWORD = "nbdpassword";

    // -------------------------------------------------------

    @FXML
    private void nextStepButtonClicked(ActionEvent event) {
        stepSimulation();
    }

    @FXML
    private void newGameButtonClicked(ActionEvent event) throws FileOperationException {
        try {
            FXMLLoader loader = new FXMLLoader(InitialConfigController.class.
                    getResource("InitialConfigScene.fxml"));
            Scene initialConfigScene = new Scene(loader.load());

            InitialConfigController initialConfigController = loader.getController();
            initialConfigController.setPrimaryStage((Stage) boardGrid.getScene().getWindow());

            Stage primaryStage = (Stage) boardGrid.getScene().getWindow();
            primaryStage.setScene(initialConfigScene);
            primaryStage.setTitle("GOL - Configuration");
        } catch (Exception e) {
            var exception = new FileOperationException("file.not-able-to-read", e);
            exception.log(logger);
            throw exception;
        }
    }

    @FXML
    private void cleanBoardButtonClicked(ActionEvent event) {
        cleanBoard();
        renderBoard();
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) throws FileOperationException {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save_Game_Of_Life_Board");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game of Life Files", ".gol"));
//
//        File file = fileChooser.showSaveDialog(boardGrid.getScene().getWindow());
//        if (file != null) {
//            try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath())) {
//                dao.write(gameBoard);
//            } catch (Exception e) {
//                var exception = new FileOperationException("file.not-able-to-write", e);
//                exception.log(logger);
//                throw exception;
//            }
//        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game Of Life Board");
        dialog.setHeaderText("Enter a name for the board:");
        dialog.setContentText("Board name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(boardName -> {
            try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao(boardName)) {
                dao.write(gameBoard);
            } catch (Exception e) {
                var exception = new FileOperationException("file.not-able-to-write", e);
                exception.log(logger);
                throw new RuntimeException(exception);
            }
        });
    }

    // -------------------------------------------------------

    public void postInitialize(GameOfLifeBoard gameBoard) {
        this.gameBoard = gameBoard;

        // resources
        resources = ResourceBundle.getBundle("Messages", java.util.Locale.getDefault());

        iterationCountLabel.setText(resources.getString("iterationCountLabel"));
        nextStepButton.setText(resources.getString("nextStepButton"));
        newGameButton.setText(resources.getString("newGameButton"));
        cleanBoardButton.setText(resources.getString("cleanBoardButton"));
        saveButton.setText(resources.getString("saveButton"));
        gameTitle.setText(resources.getString("gameTitle"));
        updateIterationLabel();

        renderBoard();
    }

    private void renderBoard() {
        boardGrid.getChildren().clear();
        int rows = gameBoard.getNumRows();
        int cols = gameBoard.getNumCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                GameOfLifeCell cell = gameBoard.getCell(r, c);
                Rectangle cellField = new Rectangle(20, 20);
                cellField.setFill(gameBoard.get(r, c) ? Color.YELLOW : Color.DARKGRAY);
                cellField.setStroke(Color.GREY);

                try {
                    JavaBeanBooleanProperty aliveProperty = JavaBeanBooleanPropertyBuilder.create()
                            .bean(cell)
                            .name("cellValue")
                            .build();

                    aliveProperty.addListener((observable, oldValue, newValue) -> {
                        cellField.setFill(newValue ? Color.YELLOW : Color.DARKGRAY);
                    });
                } catch (NoSuchMethodException e) {
                    var exception = new MethodNotFoundException("file.not-able-to-write", e);
                    exception.log(logger);
                    throw exception;
                }

                final int row = r;
                final int col = c;
                cellField.setOnMouseClicked(event -> {
                    changeCellState(row, col);
                });

                boardGrid.add(cellField, c, r);
            }
        }
    }

    public void stepSimulation() {
        gameBoard.doSimulationStep();
        iterationCount++;
        updateIterationLabel();
        renderBoard();
    }

    private void changeCellState(int row, int col) {
        boolean currentState = gameBoard.get(row, col);
        gameBoard.set(row, col, !currentState);
        renderBoard();
    }

    private void cleanBoard() {
        iterationCount = 0;
        updateIterationLabel();

        int rows = gameBoard.getNumRows();
        int cols = gameBoard.getNumCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (gameBoard.get(r, c)) {
                    gameBoard.set(r, c, false);
                }
            }
        }
    }

    private void updateIterationLabel() {
        ResourceBundle resources = ResourceBundle.getBundle("Messages", java.util.Locale.getDefault());
        String iterationText = resources.getString("iterationCountLabel");
        iterationCountLabel.setText(iterationText + " " + iterationCount);
    }


}
