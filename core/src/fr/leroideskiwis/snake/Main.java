package fr.leroideskiwis.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.snake.entities.Apple;
import fr.leroideskiwis.snake.entities.Body;
import fr.leroideskiwis.snake.entities.Entity;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main implements ApplicationListener {

    private ShapeRenderer shapeRenderer;
    private List<Entity> entities;
    private float delta;
    private int width;
    private int height;
    private Point direction;

    @Override
    public void create() {
        this.shapeRenderer = new ShapeRenderer();
        this.entities = new ArrayList<>();
        this.entities.add(new Body(Body.BodyType.HEAD, new Point(5, 5), null));
        this.entities.add(new Apple(Color.GREEN, PointUtils.getRandomPosition(10, 10)));
        Gdx.graphics.setContinuousRendering(true);
        this.direction = new Point(1, 0);

    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void checkInput(){

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) direction = new Point(1, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) direction = new Point(-1, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) direction = new Point(0, 1);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) direction = new Point(0, -1);
    }

    private List<Entity> getEntitiesCopy(){
        return new ArrayList<>(entities);
    }

    private List<Entity> getFullEntities(){
        return getEntitiesCopy().stream()
                .flatMap(entity1 -> {
                    if(entity1 instanceof Body && ((Body)entity1).isType(Body.BodyType.HEAD)){
                        return ((Body)entity1).toList(null).stream();
                    }
                    return Stream.of(entity1);
                }).collect(Collectors.toList());
    }

    private void runCollisions(){

        List<Entity> fullEntities = getFullEntities();

        for(Entity entity1 : fullEntities){
            for(Entity entity2 : fullEntities){

                if(entity1 != entity2 && entity1.hasSameLocation(entity2)) {
                    Function<List<Entity>, Point> function = entity1.onCollide(entity2);

                    if (entities.contains(entity1)) {
                        entities.remove(entity1);
                        entities.add(entity1.update(function.apply(fullEntities)));
                    }
                }

            }
        }

    }

    private void updateHead(){
        Optional<Body> bodyOptional = entities.stream()
                .filter(entity -> entity instanceof Body)
                .filter(entity -> ((Body) entity)
                        .isType(Body.BodyType.HEAD))
                .findAny()
                .map(entity -> (Body)entity);
        bodyOptional.ifPresent(body -> {
            entities.remove(body);
            entities.add(body.move(direction));
        });
    }

    @Override
    public void render() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Gdx.gl.glClearColor( 0, 0, 0, 0 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        delta+= Gdx.graphics.getDeltaTime();
        if(delta >= 0.11f){
            delta = 0;
            checkInput();
            updateHead();
            runCollisions();
        }
        entities.forEach(entity -> entity.draw(shapeRenderer, new Rectangle(0, 0, width/10, height/10)));



        shapeRenderer.end();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
