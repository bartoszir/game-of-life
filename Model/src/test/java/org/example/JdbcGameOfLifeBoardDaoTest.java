package org.example;

import org.example.exceptions.DatabaseConnectionException;
import org.example.exceptions.FileOperationException;
import org.example.exceptions.GameOfLifeDaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcGameOfLifeBoardDaoTest {
    int testNumRows = 11;
    int testNumCols = 11;
    private final GameOfLifeSimulator gameOfLifeSimulator = new PlainGameOfLifeSimulator();
    private GameOfLifeBoard gameOfLifeBoard = new GameOfLifeBoard(testNumRows, testNumCols, gameOfLifeSimulator);
    private JdbcGameOfLifeBoardDao dataSource;
    private String testBoardName = "test_board_good";
    private Connection connection;

    JdbcGameOfLifeBoardDaoTest() {
    }

    @BeforeEach
    public void setUp() {
        Locale.setDefault(new Locale("en", "EN"));
    }

    @Test
    public void readFileNotFoundTest() {
        try {
            JdbcGameOfLifeBoardDao jbdc = new JdbcGameOfLifeBoardDao("not_board");
        } catch (DatabaseConnectionException e) {
            assertEquals("Error reading from the database.", e.getMessage());
        }
    }

    @Test
    void writeReadTest() throws GameOfLifeDaoException, FileOperationException {
        dataSource = new JdbcGameOfLifeBoardDao(testBoardName);
        connection = dataSource.getConnection();
        String emptyTest = "new_board";

        JdbcGameOfLifeBoardDao dataSource2 = new JdbcGameOfLifeBoardDao(emptyTest);
        try {
            GameOfLifeBoard board = dataSource.read();
        } catch (DatabaseConnectionException e) {
            assertEquals("Error reading from the database.", e.getMessage());
        }
    }
}
