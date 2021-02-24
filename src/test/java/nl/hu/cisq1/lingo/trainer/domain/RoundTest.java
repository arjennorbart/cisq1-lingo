package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.ReachedMaxAttemptsException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {

    @Test
    @DisplayName("Should have a maxiumum of 5 attempts")
    void maxFiveAttemptsTest() {
        Round round = new Round("banana");
        round.setAttempts(6);
        assertThrows(
                ReachedMaxAttemptsException.class,
                round::maxAttemptsChecker
        );
    }

    @Test
    @DisplayName("provide starting feedback should show the first letter")
    void provideStartingHintTest() {
        Round round = new Round("banana");
        assertEquals(new Hint(List.of('b', '.', '.', '.', '.', '.')), round.provideStartingHint());
    }

    @Test
    @DisplayName("Should receive feedback after doing an attempt")
    void doAttemptTest() {
        Round round = new Round("banaan");
        Feedback feedback = new Feedback(
                "katoen",
                List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT));
        assertEquals(feedback, round.doAttempt("katoen"));
    }

    @Test
    @DisplayName("attempts should be +1 after doing an attempt")
    void attemptPlusOne() {
        Round round = new Round("banaan");
        round.doAttempt("katoen");
        assertEquals(1, round.getAttempts());
    }
}
