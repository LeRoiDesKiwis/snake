package fr.leroideskiwis.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.snake.entities.Apple;
import fr.leroideskiwis.snake.entities.Body;
import fr.leroideskiwis.snake.entities.Entity;
import fr.leroideskiwis.snake.utils.BackChecker;
import fr.leroideskiwis.snake.utils.EntityUtils;
import fr.leroideskiwis.snake.utils.PointUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Main implements ApplicationListener {
    private ShapeRenderer shapeRenderer;
    private List<Entity> entities;
    private SpriteBatch batch;
    private float delta;
    private int width;
    private int height;
    private Point direction;
    private MapSize mapSize;
    private boolean pause;
    private String text;
    private Score score;
    private BitmapFont font;

    @Override
    public void create() {
        this.mapSize = new MapSize(20, 20);
        this.shapeRenderer = new ShapeRenderer();
        this.entities = new ArrayList<>();
        this.direction = new Point(1, 0);
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.score = new Score();

        this.entities.add(new Body(score, mapSize, Body.BodyType.HEAD, new Point(mapSize.width / 2, mapSize.width / 2), null));
        this.entities.add(new Apple(score, mapSize, Color.GREEN, PointUtils.getRandomPosition(mapSize.width, mapSize.height)));

        Gdx.graphics.setContinuousRendering(true);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void checkInput() {
        BackChecker checker = new BackChecker(direction);

        if (!EntityUtils.getEntity(entities, entity -> entity instanceof Body && ((Body) entity).isType(Body.BodyType.HEAD)).get().isInBorder()) {
            return;
        }

        Point direction = null;

        // TODO Use a map instead?
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = new Point(1, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = new Point(-1, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = new Point(0, 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = new Point(0, -1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            pause = !pause;
        }

        if (checker.check(direction)) {
            this.direction = direction;
        }
    }

    // TODO Refactor it!
    private void runCollisions() {
        List<Entity> fullEntities = EntityUtils.getFullEntities(entities);

        for (Entity entity1 : fullEntities) {
            for (Entity entity2 : fullEntities) {
                if (entity1 != entity2 && entity1.hasSameLocation(entity2)) {
                    Function<List<Entity>, Point> function = entity1.onCollide(entity2);

                    if (entities.contains(entity1)) {
                        entities.remove(entity1);
                        entities.add(entity1.update(function.apply(fullEntities)));
                    }
                }
            }
        }
    }

    private void updateHead() {
        Optional<Body> bodyOptional = entities.stream()
                .filter(entity -> entity instanceof Body)
                .filter(entity -> ((Body) entity)
                        .isType(Body.BodyType.HEAD))
                .findAny()
                .map(entity -> (Body) entity);
        bodyOptional.ifPresent(body -> {
            entities.remove(body);
            entities.add(body.move(direction));
        });
    }

    public void renderText() {
        // final int TEXT_SIZE = 10;
        Gdx.graphics.setTitle("snake (" + text + ")");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        delta += Gdx.graphics.getDeltaTime();
        if (delta >= 0.1f && !pause) {
            delta = 0;
            updateHead();
            runCollisions();
        }

        if (pause) {
            text = "en pause";
        } else {
            text = "score : " + score;
        }

        checkInput();
        entities.forEach(entity -> entity.draw(shapeRenderer, new Rectangle(0, 0, width / mapSize.width, height / mapSize.height)));
        renderText();

        shapeRenderer.end();
        batch.end();
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
        font.dispose();
        batch.dispose();
    }
}
