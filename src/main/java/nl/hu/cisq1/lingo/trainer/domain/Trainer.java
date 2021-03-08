package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Trainer implements Serializable {
    private int score = 0;
    private List<Round> previousRounds;
    private GameStatus gameStatus;
    private Round activeRound;
    private boolean gameIsFinished = false;

    public Trainer() {
        this.previousRounds = new ArrayList<>();
    }

    public void startNewRound(String wordToGuess) {
        this.activeRound = new Round(wordToGuess);
        this.activeRound.provideStartingHint();
        this.gameStatus = GameStatus.PLAYING;
    }

    public void doAttempt(String attempt) {
        checkGameStatus(this.activeRound.doAttempt(attempt));
    }

    //checks the game status. If round is won the score is calculated. If round is lost the game has ended.
    private void checkGameStatus(boolean isRoundFinished) {
        if (!isRoundFinished)
            return;

        if (this.activeRound.getAttempts() >= this.activeRound.getMaxAttempts()) {
            this.gameStatus = GameStatus.ELIMINATED;
            this.gameIsFinished = true;
        } else {
            calculateScore();
            this.gameStatus = GameStatus.ROUND_WON;
        }
        this.previousRounds.add(this.activeRound);
    }

    //calculates the players score when a word is guessed correctly.
    public void calculateScore() {
        this.score += 5 * (5 - this.activeRound.getAttempts() + 5);
    }

    //this method is used for providing a random word with the right length when starting a new round.
    public int provideLengthNextWordToGuess() {
        return switch (this.activeRound.getWordToGuess().length()) {
            case 5 -> 6;
            case 6 -> 7;
            default -> 5;
        };
    }
}
