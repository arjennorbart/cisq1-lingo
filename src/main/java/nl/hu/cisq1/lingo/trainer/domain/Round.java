package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.ReachedMaxAttemptsException;
import nl.hu.cisq1.lingo.trainer.domain.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Round implements Serializable {
    private final String wordToGuess;
    private int attempts;
    private final int maxAttempts = 5;
    private boolean isFinished = false;
    private List<Feedback> feedback;
    private Hint hint;
    private Boolean wordIsGuessed;
    private AttemptValidator validator = new AttemptValidator();

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        maxAttemptsChecker();
        this.feedback = new ArrayList<>();
    }

    //Might be redundant because Trainer class also checks this.
    public void maxAttemptsChecker() {
        if (this.attempts >= this.maxAttempts)
            throw new ReachedMaxAttemptsException("Maximum of 5 attempts");
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
    public void doAttempt(String attempt) {
        List<Mark> marks = this.validator.generateMarks(this.wordToGuess, attempt);
        this.attempts += 1;
        Feedback feedback = new Feedback(attempt, marks);
        this.feedback.add(feedback);
        this.wordIsGuessed = feedback.isWordGuessed();
        this.hint = feedback.giveHint(this.hint, this.wordToGuess);
    }

    //Only shows the wordToGuess to the player when the round is finished.
    public String displayWordToPlayer() {
        if (isFinished)
            return this.wordToGuess;
        return "Word is hidden until round is finished";
    }
}
