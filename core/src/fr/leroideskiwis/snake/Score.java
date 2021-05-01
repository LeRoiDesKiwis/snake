package fr.leroideskiwis.snake;

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
