package org.example;

import java.util.HashMap;
import java.util.Map;

public class GameOfLifeBoardDaoFactory {
    private Map<String, GameOfLifeBoard> prototypes = new HashMap<>();

    public Dao<GameOfLifeBoard> getFileDao(String fileName) {
        return new FileGameOfLifeBoardDao(fileName);
    }

    public void registerPrototype(String key, GameOfLifeBoard prototype) {
        prototypes.put(key, prototype);
    }

    public void unregisterPrototype(String key) {
        prototypes.remove(key);
    }

    public GameOfLifeBoard createInstance(String key) throws CloneNotSupportedException {
        GameOfLifeBoard prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype registered with key: " + key);
        }

        return prototype.clone();
    }
}
