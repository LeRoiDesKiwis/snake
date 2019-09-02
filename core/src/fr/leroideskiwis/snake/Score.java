package fr.leroideskiwis.snake;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Score {

    private int score = 0;

    public void increment(){
        score++;
    }

    @Override
    public String toString() {
        return Integer.toString(score);
    }
}
