package org.example;

import org.example.exceptions.DatabaseConnectionException;
import org.example.exceptions.FileOperationException;
import org.example.exceptions.GameOfLifeDaoException;

import java.sql.*;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard>, AutoCloseable {
    private static final String URL = "jdbc:postgresql://localhost:5432/game_of_life";
    private static final String USER = "nbd";
    private static final String PASSWORD = "nbdpassword";

    private Connection connection;

    private final String boardName;

    public JdbcGameOfLifeBoardDao(String boardName) throws DatabaseConnectionException {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            this.boardName = boardName;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("database.connection-problem", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public GameOfLifeBoard read() throws GameOfLifeDaoException {
        String sql = "SELECT * FROM game_of_life_boards WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, boardName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                // String name = resultSet.getString("name");
                int numRows = resultSet.getInt("num_rows");
                int numCols = resultSet.getInt("num_cols");

                GameOfLifeBoard board = new GameOfLifeBoard(numRows, numCols, new PlainGameOfLifeSimulator());
                fetchCells(board, id);
                return board;
            }
        } catch (SQLException e) {
            throw new GameOfLifeDaoException("database.connection-problem", e);
        }
        return null;
    }

    private void fetchCells(GameOfLifeBoard board, int boardId) throws DatabaseConnectionException {
        String sql = "SELECT * FROM game_of_life_cells WHERE board_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, boardId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int row = resultSet.getInt("row");
                int col = resultSet.getInt("col");
                boolean value = resultSet.getBoolean("value");
                board.set(row, col, value);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("database.connection-problem", e);
        }
    }

    @Override
    public void write(GameOfLifeBoard board) throws FileOperationException {
        String sql = "INSERT INTO game_of_life_boards (name, num_rows, num_cols) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, boardName);
            statement.setInt(2, board.getNumRows());
            statement.setInt(3, board.getNumCols());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int boardId = generatedKeys.getInt(1);
                    saveCells(board, boardId);
                }
            }
        } catch (SQLException e) {
            throw new FileOperationException("file.not-able-to-write", e);
        }
    }

    private void saveCells(GameOfLifeBoard board, int boardId) {
        String sql = "INSERT INTO game_of_life_cells (board_id, row, col, value) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int row = 0; row < board.getNumRows(); row++) {
                for (int col = 0; col < board.getNumCols(); col++) {
                    statement.setInt(1, boardId);
                    statement.setInt(2, row);
                    statement.setInt(3, col);
                    statement.setBoolean(4, board.get(row, col));
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
    }
}


