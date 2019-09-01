package fr.leroideskiwis.snake.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Body extends Entity {

    private Body child;
    private BodyType type;

    public Body(BodyType type, Color color, Point point, Body child) {
        super(color, point);
        this.child = child;
        this.type = type;
    }

    public Body(BodyType type, Point point, Body child) {
        this(type, type.color, point, child);
    }

    @Override
    public Body update(Point newPosition) {

        if (child != null && !point.equals(child.point)) {
            child = child.update(point);
        }

        if(PointUtils.isInBorder(newPosition, 10, 10)) return new Body(type, newPosition, child);
        else return this;
    }

    @Override
    public Function<List<Entity>, Point> onCollide(Entity entity) {
        if(entity instanceof Body){

            if(child != null) ((Body) entity).kill();
            return entities -> point;

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
            this.child = new Body(BodyType.TAIL, this.point, null);
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
