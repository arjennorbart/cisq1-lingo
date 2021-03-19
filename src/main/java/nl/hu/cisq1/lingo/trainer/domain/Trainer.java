package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainer")
@Getter
@Setter
public class Trainer implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private int score = 0;
    private GameStatus gameStatus;
    private boolean gameIsFinished = false;

    @OneToOne(cascade = CascadeType.ALL)
    private Round activeRound;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Round> previousRounds;

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
    private void checkGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        if (this.activeRound.isFinished()) {
            if (this.gameStatus.equals(GameStatus.ROUND_WON))
                calculateScore();
            this.previousRounds.add(this.activeRound);
        }
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
