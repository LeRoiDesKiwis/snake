package fr.leroideskiwis.snake.utils;

import fr.leroideskiwis.snake.entities.Entity;

import java.awt.Point;
import java.util.List;
import java.util.Optional;

public class Utils {
    public static Optional<Entity> getEntityAt(Point point, List<Entity> entities) {
        return entities.stream().filter(entity -> entity.isLocation(point)).limit(1).findAny();
    }

    public int compare(int i, int j) {
        return i - j;
    }
}
