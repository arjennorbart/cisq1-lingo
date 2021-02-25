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
    private List<Feedback> feedback;
    private Hint hint;
    private Boolean wordIsGuessed;

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        maxAttemptsChecker();
        this.feedback = new ArrayList<>();
    }

    public void maxAttemptsChecker() {
        if (this.attempts >= this.maxAttempts)
//            throw new ReachedMaxAttemptsException("Maximum of 5 attempts");
            System.out.println("throws exception in Round line 34 maxAttemptsChecker()");
    }

    public void provideStartingHint() {
        List<Character> startingHintList = new ArrayList<>();
        char[] wordToGuessArray = wordToGuess.toCharArray();
        startingHintList.add(wordToGuessArray[0]);
        for (int i = 0; i < wordToGuessArray.length - 1; i++)
            startingHintList.add(Utils.dot());
        this.hint = new Hint(startingHintList);
    }

    public void doAttempt(String attempt) {
        //TODO: make class for generating marks from the attempt and validating.
        // Also fix Mark.PRESENT check (if char only appears once)
        List<Mark> marks = new ArrayList<>();
        char[] wordToGuessArray = this.wordToGuess.toCharArray();
        char[] attemptArray = attempt.toCharArray();
        for (int i = 0; i < attemptArray.length; i++) {
            if (wordToGuessArray[i] == attemptArray[i])
                marks.add(Mark.CORRECT);
            else if (wordToGuess.contains(String.valueOf(attemptArray[i])))
                marks.add(Mark.PRESENT);
            else marks.add(Mark.ABSENT);
        }
        this.attempts += 1;
        maxAttemptsChecker();
        Feedback feedback = new Feedback(attempt, marks);
        this.feedback.add(feedback);
        this.wordIsGuessed = feedback.isWordGuessed();
        this.hint = feedback.giveHint(hint, this.wordToGuess);
    }
}
