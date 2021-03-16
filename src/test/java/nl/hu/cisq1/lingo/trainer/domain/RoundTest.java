package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.ReachedMaxAttemptsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {

    @Test
    @DisplayName("provide starting feedback should show the first letter")
    void provideStartingHintTest() {
        Round round = new Round("banana");
        round.provideStartingHint();
        assertEquals(new Hint(List.of('b', '.', '.', '.', '.', '.')), round.getHint());
    }

    @Test
    @DisplayName("Should receive feedback after doing an attempt")
    void doAttemptTest() {
        Round round = new Round("banaan");
        round.provideStartingHint();
        round.doAttempt("katoen");
        System.out.println(round.getFeedbackList());
        Feedback feedback = new Feedback(
                "katoen",
                List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT));
        assertEquals(feedback, round.getFeedbackList().get(round.getFeedbackList().size() - 1));
    }

    @Test
    @DisplayName("attempts should be +1 after doing an attempt")
    void attemptPlusOne() {
        Round round = new Round("banaan");
        round.provideStartingHint();
        round.doAttempt("katoen");
        assertEquals(1, round.getAttempts());
    }

    @Test
    @DisplayName("Should show the word to guess when the round is finished")
    void displayWordToPlayer() {
        Round round = new Round("banana");
        round.setFinished(true);
        assertEquals("banana", round.displayWordToPlayer());
    }

    @Test
    @DisplayName("Should show 'word is hidden until round is finished' to player ")
    void notDisplayWordToPlayer() {
        Round round = new Round("banana");
        assertEquals("Word is hidden until round is finished", round.displayWordToPlayer());
    }
}
