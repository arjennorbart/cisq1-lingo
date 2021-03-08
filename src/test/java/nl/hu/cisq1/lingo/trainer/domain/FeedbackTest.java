package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
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
        assertDoesNotThrow(
                () -> new Feedback("woord", List.of(Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))
        );
    }

    @Test
    @DisplayName("guess is not valid if it does not contain the right amount of characters")
    void guessIsNotValid() {
        List<Mark> list = List.of(Mark.CORRECT);
        assertThrows(
                InvalidAttemptLengthException.class,
                () -> new Feedback("woord", list)
        );
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Show all marks when the attempt is correct")
    void provideFeedback(String wordToGuess, List<Mark> marks, Hint previousHint, Hint newHint) {
        Feedback feedback = new Feedback("banana", marks);
        assertEquals(newHint, feedback.giveHint(previousHint, wordToGuess));
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('.', '.', '.', '.', '.', '.')),
                        new Hint(List.of('b', 'a', 'n', 'a', 'n', 'a'))),
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT),
                        new Hint(List.of('.', '.', '.', '.', '.', '.')),
                        new Hint(List.of('b', '.', '.', '.', '.', '.'))),
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT),
                        new Hint(List.of('.', '.', '.', '.', '.', '.')),
                        new Hint(List.of('b', '.', '+', '.', '.', 'a'))),
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT),
                        new Hint(List.of('.', '.', '.', '.', '.', '.')),
                        new Hint(List.of('b', '.', '+', '+', '.', 'a'))),
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT),
                        new Hint(List.of('.', '.', '.', '.', '.', '.')),
                        new Hint(List.of('b', '.', '.', '.', '.', 'a'))),
                Arguments.of("banana", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT),
                        new Hint(List.of('.', '.', '.', '.', '.', 'a')),
                        new Hint(List.of('b', '.', '.', '.', '.', 'a'))),
                Arguments.of("banaan", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT),
                        //previous hint is "katoen"
                        new Hint(List.of('.', 'a', '.', '.', '.', 'n')),
                        //if the attempt is "kanaal" the feedback should be ".anaan" and not ".anaa."
                        new Hint(List.of('.', 'a', 'n', 'a', 'a', 'n')))
        );
    }
}
