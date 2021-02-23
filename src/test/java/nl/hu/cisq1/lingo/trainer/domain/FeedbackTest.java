package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("guess is valid if it contains the right amount of characters")
    void guessIsValid() {
        Feedback feedback = new Feedback("woord", List.of(Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.guessIsValid());
    }

    @Test
    @DisplayName("guess is not valid if it does not contain the right amount of characters")
    void guessIsNotValid() {
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", List.of(Mark.CORRECT))
        );
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Show all marks when the attempt is correct")
    void provideFeedback(String wordToGuess, List<Mark> marks) {
        Feedback feedback = new Feedback("banana", marks);
        assertEquals(wordToGuess, feedback.giveHint());
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("b.....", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("b.+..a", List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT)),
                Arguments.of("b.++.a", List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT)),
                Arguments.of("b....a", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT))
        );
    }

    @Test
    @DisplayName("Add previous hint to the new hint. should add the final a of banana")
    void provideFeedbackWithPreviousHint() {
        Feedback feedback = new Feedback("banana", List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT));
        feedback.getHint().setPreviousHint("b....a");
        assertEquals("b.na.a", feedback.giveHint());
    }
}
