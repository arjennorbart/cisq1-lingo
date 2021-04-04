package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import lombok.Setter;
import nl.hu.cisq1.lingo.trainer.domain.utils.Utils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "round")
@Getter
@Setter
public class Round {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private static final int MAX_ATTEMPTS = 4;
    private String wordToGuess;
    private int attempts;
    private boolean isFinished = false;
    private Boolean wordIsGuessed;
    private Hint hint;
    private AttemptValidator validator = new AttemptValidator();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbackList;

    public Round() {
    }

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.feedbackList = new ArrayList<>();
    }

    //Makes the first hint which returns te first letter of the wordToGuess
    //and fills the rest of the length of the wordToGuess with dots.
    public void provideStartingHint() {
        List<Character> startingHintList = new ArrayList<>();
        char[] wordToGuessArray = wordToGuess.toCharArray();
        startingHintList.add(wordToGuessArray[0]);
        for (int i = 0; i < wordToGuessArray.length - 1; i++)
            startingHintList.add(Utils.dot());
        this.hint = new Hint(startingHintList);
    }

    //Generates a List with marks from the attempt and provides feedback and a hint.
    public GameStatus doAttempt(String attempt) {
        GameStatus gameStatus;
        List<Mark> marks = this.validator.generateMarks(this.wordToGuess, attempt);
        Feedback feedback = new Feedback(attempt, marks);
        this.wordIsGuessed = feedback.isWordGuessed();
        gameStatus = finishedStatus();
        this.hint = feedback.giveHint(this.hint, this.wordToGuess);
        this.attempts++;
        this.feedbackList.add(feedback);
        return gameStatus;
    }

    public GameStatus finishedStatus() {
        if (Boolean.TRUE.equals(this.wordIsGuessed)) {
            this.isFinished = true;
            return GameStatus.ROUND_WON;
        } else if (this.attempts >= MAX_ATTEMPTS) {
            this.isFinished = true;
            return GameStatus.ELIMINATED;
        }
        return GameStatus.PLAYING;
    }

    //Only shows the wordToGuess to the player when the round is finished.
    public String displayWordToPlayer() {
        if (this.isFinished)
            return this.wordToGuess;
        return "Word is hidden until round is finished";
    }
}
