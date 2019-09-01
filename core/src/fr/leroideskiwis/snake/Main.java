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
    private MapSize mapSize;

    @Override
    public void create() {

        this.mapSize = new MapSize(20, 20);
        this.shapeRenderer = new ShapeRenderer();
        this.entities = new ArrayList<>();
        this.direction = new Point(1, 0);

        this.entities.add(new Body(mapSize, Body.BodyType.HEAD, new Point(mapSize.width/2, mapSize.width/2), null));
        this.entities.add(new Apple(mapSize, Color.GREEN, PointUtils.getRandomPosition(mapSize.width, mapSize.height)));

        Gdx.graphics.setContinuousRendering(true);

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
        checkInput();
        entities.forEach(entity -> entity.draw(shapeRenderer, new Rectangle(0, 0, width/mapSize.width, height/mapSize.height)));

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
