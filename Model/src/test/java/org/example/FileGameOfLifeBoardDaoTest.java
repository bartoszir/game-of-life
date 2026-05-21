package org.example;

import org.example.exceptions.FileOperationException;
import org.example.exceptions.GameOfLifeDaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class FileGameOfLifeBoardDaoTest {
    int testNumRows = 11;
    int testNumCols = 11;
    //    int testNumberOfLiveCells = 15;
    //String testName = "gameBoard";
    private final GameOfLifeSimulator gameOfLifeSimulator = new PlainGameOfLifeSimulator();
    private GameOfLifeBoardDaoFactory factory = new GameOfLifeBoardDaoFactory();
    private GameOfLifeBoard gameOfLifeBoard = new GameOfLifeBoard(testNumRows, testNumCols, gameOfLifeSimulator);

    private Dao<GameOfLifeBoard> fileGameOfLifeDao;
    private GameOfLifeBoard gameOfLifeBoardSecond;

    FileGameOfLifeBoardDaoTest() {
        gameOfLifeBoard.doSimulationStep();
    }

    @BeforeEach
    public void setUp() {
        Locale.setDefault(new Locale("en", "EN"));
    }

    @Test
    public void writeReadTest() throws FileOperationException, GameOfLifeDaoException {
        fileGameOfLifeDao = factory.getFileDao("xxx");
        fileGameOfLifeDao.write(gameOfLifeBoard);

        File file = new File("xxx");
        assertTrue(file.exists());

        gameOfLifeBoardSecond = fileGameOfLifeDao.read();
        assertEquals(gameOfLifeBoard, gameOfLifeBoardSecond);
        file.delete();
    }

    @Test
    public void readFileNotFoundTest() {
        FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao("thereIsNoFile");

        Exception exception = assertThrows(FileOperationException.class, dao::read);

        assertEquals("Error when trying to open a file. " +
                "Make sure that the file existsand program has " +
                "the rights to read it.", exception.getMessage());
    }

    @Test
    public void testIOExceptionDuringRead() {
        String testFileName = "test.txt";

        try (FileOutputStream fos = new FileOutputStream(testFileName)) {
            fos.write("Dane testowe".getBytes()); // Piszemy dane, ale nie zamykamy strumienia
            FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(testFileName);

            // Oczekujemy, że odczyt rzuci RuntimeException z powodu IOException
            Exception exception = assertThrows(FileOperationException.class, dao::read);

            // Weryfikacja treści komunikatu błędu
            assertEquals("Error when trying to open a file. " +
                    "Make sure that the file existsand program has " +
                    "the rights to read it.", exception.getMessage());
        } catch (IOException e) {
            fail("Test nie powiódł się z powodu błędu zapisu: " + e.getMessage());
        }
        File file = new File(testFileName);
        file.delete();
    }

    @Test
    public void readExceptionTest() {
        Exception exception = assertThrows(FileOperationException.class, () -> {
            Dao<GameOfLifeBoard> fileGameOfLifeDao;
            fileGameOfLifeDao = factory.getFileDao("Case2");
            fileGameOfLifeDao.read();
        });
    }

    @Test
    public void closeTest() {
        assertDoesNotThrow(() -> {
            try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao("xxx")) {
            }
        });
    }


    /*@Test
    public void readExceptionTest() {
        Exception exception = assertThrows(ClassNotFoundException.class, () -> {
            Dao<GameOfLifeBoard> fileGameOfLifeDao;
            fileGameOfLifeDao = factory.getFileDao("Case2");
            fileGameOfLifeDao.read();
        });
    }

    @Test
    public void readNotAClassExceptionTest() {
        Exception exception = assertThrows(ClassNotFoundException.class, () -> {
            Dao<GameOfLifeBoard> fileGameOfLifeDao;
            fileGameOfLifeDao = factory.getFileDao("readTest");
            GameOfLifeBoard test = fileGameOfLifeDao.read();
        });
    }

    @Test
    public void writeExceptionTest() {
        Exception exception = assertThrows(ClassNotFoundException.class, () -> {
            Dao<GameOfLifeBoard> fileGameOfLifeDao;
            GameOfLifeBoard sudokuBoard = new GameOfLifeBoard(testNumRows, testNumCols, testNumberOfLiveCells, gameOfLifeSimulator);
            fileGameOfLifeDao = factory.getFileDao("/?;:");
            fileGameOfLifeDao.write(sudokuBoard);
        });
    }*/
}
