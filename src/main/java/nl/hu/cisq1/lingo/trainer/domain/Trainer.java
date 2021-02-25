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
    private boolean isFinished = false;

    public Trainer() {
        this.rounds = new ArrayList<>();
    }

    public void wordValidator(String word) {
        //word needs to be 5, 6 or 7 length depending on the round.
    }

    public void startNewRound(String wordToGuess) {
        if (this.activeRound != null)
            this.rounds.add(this.activeRound);
        this.activeRound = new Round(wordToGuess);
        this.activeRound.provideStartingHint();
    }

    public void doAttempt(String attempt) {
        //TODO: call wordValidator and implement more validation
        this.activeRound.doAttempt(attempt);
    }

    public void isGameFinished() {
        if (this.activeRound.getWordIsGuessed() || this.activeRound.getAttempts() >= this.activeRound.getMaxAttempts()) {
            this.rounds.add(this.activeRound);
            this.gameStatus = GameStatus.ELIMINATED;
            this.isFinished = true;
        }
    }
}
