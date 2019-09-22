package fr.leroideskiwis.snake.entities;

import com.badlogic.gdx.graphics.Color;
import fr.leroideskiwis.snake.MapSize;
import fr.leroideskiwis.snake.Score;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Apple extends Entity {
    public Apple(Score score, MapSize size, Color color, Point point) {
        super(score, size, color, point);
    }

    @Override
    public Apple update(Point newPosition) {
        if (PointUtils.isInBorder(newPosition, mapSize.width, mapSize.height)) {
            return new Apple(score, mapSize, color, newPosition);
        } else {
            return this;
        }
    }

    @Override
    public Function<List<Entity>, Point> onCollide(Entity entity) {
        if (entity instanceof Body) {
            Body body = (Body) entity;
            body.growTail();
            score.increment();
            return entities -> PointUtils.getRandomPositionExclude(mapSize.width, mapSize.height, entities.stream()
                    .map(entity1 -> entity1.point)
                    .collect(Collectors.toList()));
        }
        return super.onCollide(entity);
    }
}
