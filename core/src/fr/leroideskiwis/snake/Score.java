package fr.leroideskiwis.snake;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import fr.leroideskiwis.snake.entities.Body;

public class Score {
    private int score = 0;

    @Override
    public String toString() {
        return Integer.toString(score);
    }

    public void increment() {
        score++;
    }

    public void reset() {
        this.score = 0;
    }
}
