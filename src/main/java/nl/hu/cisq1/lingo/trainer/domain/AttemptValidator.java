package nl.hu.cisq1.lingo.trainer.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AttemptValidator implements Serializable {

    //Generates a list of marks by comparing the attempt and wordToGuess.
    public List<Mark> generateMarks(String wordToGuess, String attempt) {
        if (wordToGuess.length() != attempt.length())
            throw new InvalidAttemptLengthException("Invalid length");
        List<Mark> marks = new ArrayList<>();
        char[] wordToGuessArray = wordToGuess.toCharArray();
        char[] attemptArray = attempt.toCharArray();
        for (int i = 0; i < attemptArray.length; i++) {
            if (wordToGuessArray[i] == attemptArray[i])
                marks.add(Mark.CORRECT);
            else if (wordToGuess.contains(String.valueOf(attemptArray[i]))) {
                //TODO count amount of present characters and return right amount of Mark.PRESENT chars
                marks.add(Mark.PRESENT);
            }
            else marks.add(Mark.ABSENT);
        }
        return marks;
    }
}
