package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Trainer implements Serializable {
    private int score = 0;
    private List<Round> rounds;
    private GameStatus gameStatus;
    private Round activeRound;
    private boolean gameIsFinished = false;

    public Trainer() {
        this.rounds = new ArrayList<>();
    }

    public void startNewRound(String wordToGuess) {
        if (this.activeRound != null)
            this.rounds.add(this.activeRound);
        this.activeRound = new Round(wordToGuess);
        this.activeRound.provideStartingHint();
        this.gameStatus = GameStatus.PLAYING;
    }

    public void doAttempt(String attempt) {
        this.activeRound.doAttempt(attempt);
        checkGameStatus();
    }

    //checks the game status. If round is won the score is calculated. If round is lost the game has ended.
    public void checkGameStatus() {
        if (this.activeRound.getWordIsGuessed()) {
            calculateScore();
            this.gameStatus = GameStatus.ROUND_WON;
            this.activeRound.setFinished(true);
            this.rounds.add(this.activeRound);
        }
        else if (this.activeRound.getAttempts() >= this.activeRound.getMaxAttempts()) {
            this.gameStatus = GameStatus.ELIMINATED;
            this.activeRound.setFinished(true);
            this.rounds.add(this.activeRound);
            this.gameIsFinished = true;
        }
    }

    //calculates the players score when a word is guessed correctly.
    public void calculateScore() {
        this.score += 5 * (5 - this.activeRound.getAttempts() + 5);
    }
}
