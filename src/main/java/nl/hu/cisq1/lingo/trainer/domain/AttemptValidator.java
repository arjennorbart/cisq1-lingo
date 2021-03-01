package nl.hu.cisq1.lingo.trainer.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AttemptValidator implements Serializable {

    //Generates a list of marks by comparing the attempt and wordToGuess.
    public List<Mark> generateMarks(String wordToGuess, String attempt) {
        int markIsPresentCalls = 0;
        if (wordToGuess.length() != attempt.length())
            throw new InvalidAttemptLengthException("Invalid length");
        List<Mark> marks = new ArrayList<>();
        char[] wordToGuessArray = wordToGuess.toCharArray();
        char[] attemptArray = attempt.toCharArray();
        for (int i = 0; i < attemptArray.length; i++) {
            if (wordToGuess.contains(String.valueOf(attemptArray[i]))) {
                if (wordToGuessArray[i] == attemptArray[i]) {
                    marks.add(Mark.CORRECT);
                    markIsPresentCalls += 1;
                    continue;
                }
                markIsPresentCalls += 1;
                marks.add(markIsPresentValidator(wordToGuess, attempt, attemptArray[i], markIsPresentCalls));
            } else marks.add(Mark.ABSENT);
        }
        return marks;
    }

    //Checks if the mark should be present, based on the occurrences in the wordToGuess ant the attempt
    private Mark markIsPresentValidator(String wordToeGuess, String attempt, char presentChar, int markIsPresentCalls) {
        int occuranceInWordToGuess = StringUtils.countOccurrencesOf(wordToeGuess, String.valueOf(presentChar));
        int occuranceInAttempt = StringUtils.countOccurrencesOf(attempt, String.valueOf(presentChar));

        //If length is the same, then Mark.PRESENT can be applied everytime we call this method.
        if (occuranceInWordToGuess == occuranceInAttempt)
            return Mark.PRESENT;

        if (markIsPresentCalls <= occuranceInWordToGuess)
            return Mark.PRESENT;

        //if this method is called more than the character occurs in the wordToGuess, it should be absent
        return Mark.ABSENT;
    }
}
