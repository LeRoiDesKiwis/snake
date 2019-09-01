package fr.leroideskiwis.snake.entities;

import com.badlogic.gdx.graphics.Color;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Apple extends Entity {

    public Apple(Color color, Point point) {
        super(color, point);
    }

    @Override
    public Apple update(Point newPosition) {

        if(PointUtils.isInBorder(newPosition, 10, 10)) return new Apple(color, newPosition);
        else return this;
    }

    @Override
    public Function<List<Entity>, Point> onCollide(Entity entity) {
        if(entity instanceof Body){
            Body body = (Body)entity;
            if(body.isType(Body.BodyType.HEAD)) {
                body.growTail();

                return entities -> PointUtils.getRandomPositionExclude(10, 10, entities.stream()
                            .map(entity1 -> entity1.point)
                            .collect(Collectors.toList()));
            }

        }
        return super.onCollide(entity);
    }

}
