package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;
import java.util.Objects;

public class Hint {
    private static final char dot = ".".charAt(0);
    private static final char plus = "+".charAt(0);

    private String previousHint;

    public Hint() {
        this.previousHint = "";
    }

    public void setPreviousHint(String previousHint) {
        this.previousHint = previousHint;
    }

    public String giveNewHint(String attempt, List<Mark> marks) {
        StringBuilder feedbackCharacters = new StringBuilder();
        char[] attemptArray = attempt.toCharArray();
        char[] previousHintArray = this.previousHint.toCharArray();

        for (int i = 0; i < attemptArray.length; i++) {
            if (marks.get(i).equals(Mark.CORRECT)) {
                feedbackCharacters.append(attemptArray[i]);
            }
            else if (marks.get(i).equals(Mark.PRESENT)) {
                feedbackCharacters.append(plus);
            }
            else {
                if (!this.previousHint.isBlank() && previousHintArray[i] != dot && previousHintArray[i] != plus)
                    feedbackCharacters.append(previousHintArray[i]);
                else feedbackCharacters.append(dot);
            }
        }
        this.previousHint = feedbackCharacters.toString();
        return this.previousHint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint = (Hint) o;
        return Objects.equals(previousHint, hint.previousHint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(previousHint);
    }
}
