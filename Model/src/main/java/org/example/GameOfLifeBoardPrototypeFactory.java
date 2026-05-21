package org.example;

import java.util.HashMap;
import java.util.Map;

public class GameOfLifeBoardPrototypeFactory {
    private Map<String, GameOfLifeBoard> prototypes = new HashMap<>();

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
