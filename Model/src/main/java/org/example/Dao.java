package org.example;


import org.example.exceptions.FileOperationException;
import org.example.exceptions.GameOfLifeDaoException;

public interface Dao<T> extends AutoCloseable {
    T read() throws GameOfLifeDaoException, FileOperationException;

    void write(T obj) throws FileOperationException;
}
