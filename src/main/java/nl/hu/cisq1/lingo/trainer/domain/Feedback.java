package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.List;
import java.util.Objects;

public class Feedback {
    private String attempt;
    private List<Mark> marks;
    private Hint hint;

    public Feedback(String attempt, List<Mark> marks) {
        if (attempt.length() != marks.size())
            throw new InvalidFeedbackException("Invalid length");
        this.attempt = attempt;
        this.marks = marks;
        this.hint = new Hint();
    }

    public Hint getHint() {
        return hint;
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(mark -> mark == Mark.CORRECT);
    }

    public boolean guessIsValid() {
        return this.attempt.length() == this.marks.size();
    }

    public String giveHint() {
        return this.hint.giveNewHint(this.attempt, this.marks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) &&
                Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                '}';
    }
}
