package nl.hu.cisq1.lingo.trainer.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

@Getter
@NoArgsConstructor
public class AttemptValidator implements Serializable {

    //Generates a list of marks by comparing the attempt and wordToGuess.
    public List<Mark> generateMarks(String wordToGuess, String attempt) {

        if (wordToGuess.length() != attempt.length())
            throw new InvalidAttemptLengthException("Invalid length");

        char[] wordToGuessArray = wordToGuess.toCharArray();
        char[] attemptArray = attempt.toCharArray();
        List<Mark> marks = new ArrayList<>();
        HashMap<Character, Integer> map = new HashMap<>();

        List<Character> correctChars = new ArrayList<>();

        //this loop is needed to fill the correctChars, which is needed for calculating the present characters correctly
        for (int o = 0; o < attemptArray.length; o++) {
            if (wordToGuessArray[o] == attemptArray[o]) {
                marks.add(Mark.CORRECT);
                correctChars.add(attemptArray[o]);
            }
            else marks.add(Mark.INVALID);
        }

        for (int i = 0; i < attemptArray.length; i++) {
            if (marks.get(i) != Mark.CORRECT) {
                int occurrenceInWordToGuess = StringUtils.countOccurrencesOf(wordToGuess, String.valueOf(attemptArray[i]));
                occurrenceInWordToGuess -= Collections.frequency(correctChars, attemptArray[i]);
                map.merge(attemptArray[i], 1, Integer::sum);

                if (wordToGuess.contains(String.valueOf(attemptArray[i]))) {
                    if (map.get(attemptArray[i]) > occurrenceInWordToGuess) {
                        marks.set(i, Mark.ABSENT);
                        continue;
                    }
                    marks.set(i, Mark.PRESENT);
                    continue;
                }
                marks.set(i, Mark.ABSENT);
            }
        }
        return marks;
    }
}
