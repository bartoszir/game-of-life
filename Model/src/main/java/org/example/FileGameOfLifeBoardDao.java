package org.example;

import org.example.exceptions.FileOperationException;
import org.example.exceptions.GameOfLifeCellNullException;
import org.example.exceptions.GameOfLifeDaoException;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileGameOfLifeBoardDao implements Dao<GameOfLifeBoard>, AutoCloseable {

    private final String fileName;

    public FileGameOfLifeBoardDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public GameOfLifeBoard read() throws GameOfLifeDaoException, FileOperationException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (GameOfLifeBoard) ois.readObject();
        } catch (FileNotFoundException e) {
            //throw new IllegalArgumentException("Plik nie znaleziony: " + fileName, e);
            throw new FileOperationException("file.not-able-to-read", e);
        } catch (IOException e) {
            //throw new RuntimeException("Błąd odczytu z pliku: " + fileName);
            throw new FileOperationException("file.not-able-to-read", e);
        } catch (ClassNotFoundException e) {
            //throw new RuntimeException("Klasa GameOfLifeBoard nie została znaleziona: ");
            throw new GameOfLifeDaoException("class.not-found", e);
        }
    }

    @Override
    public void write(GameOfLifeBoard gameOfLifeBoard) throws FileOperationException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(gameOfLifeBoard);
        } catch (FileNotFoundException e) {
            //throw new IllegalArgumentException("Plik nie znaleziony do zapisu: " + fileName, e);
            throw new FileOperationException("file.not-able-to-write", e);
        } catch (IOException e) {
            //throw new RuntimeException("Błąd zapisu do pliku: " + fileName, e);
            throw new FileOperationException("file.not-able-to-write", e);
        }
    }

    @Override
    public void close() {
    }
}

