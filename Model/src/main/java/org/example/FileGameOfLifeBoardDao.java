package org.example;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileGameOfLifeBoardDao implements Dao<GameOfLifeBoard>, AutoCloseable {

    private final String fileName;

    public FileGameOfLifeBoardDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public GameOfLifeBoard read() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (GameOfLifeBoard) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Plik nie znaleziony: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu z pliku: " + fileName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Klasa GameOfLifeBoard nie została znaleziona: ");
        }
    }

    @Override
    public void write(GameOfLifeBoard gameOfLifeBoard) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(gameOfLifeBoard);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Plik nie znaleziony do zapisu: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Błąd zapisu do pliku: " + fileName, e);
        }
    }

    @Override
    public void close() {
    }
}

