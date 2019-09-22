package fr.leroideskiwis.snake.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.snake.MapSize;
import fr.leroideskiwis.snake.Score;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Entity {
    protected Point point;
    protected Color color;
    protected MapSize mapSize;
    protected Score score;

    public Entity(Score score, MapSize size, Color color, Point point) {
        this.point = point;
        this.color = color;
        this.mapSize = size;
        this.score = score;
    }

    public abstract Entity update(Point newPosition);

    public boolean isLocation(Point point) {
        return this.point.equals(point);
    }

    public void draw(ShapeRenderer renderer, Rectangle rectangle) {
        renderer.setColor(color);
        renderer.rect(point.x * rectangle.width, point.y * rectangle.height, rectangle.width, rectangle.height);
    }

    public Entity move(Point move) {
        return update(PointUtils.copyAndMove(point, move.x, move.y));
    }

    public Function<List<Entity>, Point> onCollide(Entity entity) {
        return entity1 -> point;
    }

    public boolean hasSameLocation(Entity entity) {
        return entity.point.equals(point);
    }

    public boolean canSpawn(List<Entity> entities, Point point) {
        return true;
    }

    public boolean isInBorder() {
        return PointUtils.isInBorder(point, mapSize.width, mapSize.height);
    }
}
