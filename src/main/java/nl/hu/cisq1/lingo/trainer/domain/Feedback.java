package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.trainer.domain.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class Feedback {

    private String attempt;
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        if (attempt.length() != marks.size())
            throw new InvalidFeedbackException("Invalid length");
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(mark -> mark == Mark.CORRECT);
    }

    public Hint giveHint(Hint previousHint, String wordToGuess) {
        char[] wordToGuessArray = wordToGuess.toCharArray();
        StringBuilder newHint = new StringBuilder();
        for (int i = 0; i < this.marks.size(); i++) {
            if (this.marks.get(i).equals(Mark.CORRECT))
                newHint.append(wordToGuessArray[i]);
            else if (this.marks.get(i).equals(Mark.PRESENT))
                newHint.append(Utils.plus());
            else newHint.append(Utils.dot());
        }
        previousHint.replaceHint(newHint.toString().chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        return previousHint;
    }
}
