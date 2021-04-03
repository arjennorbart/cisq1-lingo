package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import nl.hu.cisq1.lingo.trainer.domain.utils.Utils;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "feedback")
@EqualsAndHashCode
@Getter
@ToString
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name = "roundId")
    private Long id;
    private String attempt;

    @ElementCollection
    private List<Mark> marks;

    public Feedback() {
    }

    public Feedback(String attempt, List<Mark> marks) {
        if (attempt.length() != marks.size())
            throw new InvalidAttemptLengthException("Invalid length");
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(mark -> mark == Mark.CORRECT);
    }

    //Makes a new hint from the attempt Marks from the new attempt and then calls Hint.replaceHint() to update the hint.
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
