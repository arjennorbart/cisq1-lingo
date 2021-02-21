package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.List;
import java.util.Objects;

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

    public boolean guessIsValid() {
        return this.attempt.length() == this.marks.size();
    }

    public String giveHint() {
        StringBuilder feedbackCharacters = new StringBuilder();
        char[] attemptArray = this.attempt.toCharArray();
        for (int i = 0; i < attemptArray.length; i++) {
            if (this.marks.get(i).equals(Mark.CORRECT)) {
                feedbackCharacters.append(attemptArray[i]);
            }
            else if (this.marks.get(i).equals(Mark.PRESENT)) {
                //TODO: Implement this somehow
            }
            else {
                feedbackCharacters.append(".".charAt(0));
            }
        }
        return feedbackCharacters.toString();
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
