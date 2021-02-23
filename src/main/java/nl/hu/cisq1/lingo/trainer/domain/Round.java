package nl.hu.cisq1.lingo.trainer.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Round {
    private String wordToGuess;
    private int attempts;
    private List<Feedback> feedback;

    public String doAttempt() {
        return "";
    }
}
