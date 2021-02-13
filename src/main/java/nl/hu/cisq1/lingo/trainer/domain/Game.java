package nl.hu.cisq1.lingo.trainer.domain;

public class Game {
    private int score;
    private Round round;
    private GameStatus gameStatus;

    public Game(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
