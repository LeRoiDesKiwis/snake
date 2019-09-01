package fr.leroideskiwis.snake.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.snake.MapSize;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Body extends Entity {

    private Body child;
    private BodyType type;
    private boolean gameOver;

    public Body(MapSize size, BodyType type, Color color, Point point, Body child) {
        super(size, color, point);
        this.child = child;
        this.type = type;
    }

    public Body(MapSize size, BodyType type, Point point, Body child) {
        this(size, type, type.color, point, child);
    }

    @Override
    public Body update(Point newPosition) {

        if (child != null && !point.equals(child.point)) {
            child = child.update(point);
        }

        if(PointUtils.isInBorder(newPosition, mapSize.width, mapSize.height)) return new Body(mapSize, type, newPosition, child);
        else return this;
    }

    @Override
    public Function<List<Entity>, Point> onCollide(Entity entity) {
        if(entity instanceof Body){

            if(child != null) {
                child.kill();
                gameOver = true;
                return entities -> new Point(mapSize.width/2, mapSize.height/2);
            }

        }
        return super.onCollide(entity);
    }

    @Override
    public void draw(ShapeRenderer renderer, Rectangle rectangle) {
        super.draw(renderer, rectangle);
        if(child != null) child.draw(renderer, rectangle);
    }

    public void growTail(){
        if(child != null) child.growTail();
        else {
            this.child = new Body(mapSize, BodyType.TAIL, this.point, null);
        }
    }

    public boolean isType(BodyType bodyType){
        return type == bodyType;
    }

    public void kill() {
        if(child != null) child.kill();
        this.child = null;
    }

    public enum BodyType{
        HEAD(Color.RED), TAIL(Color.ORANGE);

        Color color;

        BodyType(Color color) {
            this.color = color;
        }
    }

    public List<Body> toList(List<Body> bodies){
        if(bodies == null) bodies = new ArrayList<>();
        bodies.add(this);
        if(child != null) return child.toList(bodies);
        else return bodies;
    }
}
