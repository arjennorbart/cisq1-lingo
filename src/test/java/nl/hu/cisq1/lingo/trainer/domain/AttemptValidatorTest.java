package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AttemptValidatorTest {

    @Test
    @DisplayName("Should throw InvalidAttemptLengthException when word to guess and attempt are not the same length")
    void shouldThrowInvalidAttemptLengthException() {
        AttemptValidator validator = new AttemptValidator();
        assertThrows(
                InvalidAttemptLengthException.class,
                () -> validator.generateMarks("banaan", "bananen")
        );
    }

    @Test
    @DisplayName("Should not throw an error when word to guess and attempt are the same length")
    void shouldNotThrowInvalidAttemptLengthException() {
        AttemptValidator validator = new AttemptValidator();
        assertDoesNotThrow(
                () -> validator.generateMarks("banaan", "banden")
        );
    }

    @ParameterizedTest
    @MethodSource("provideWordsAndAttempts")
    @DisplayName("Returns a correct List of Marks")
    void provideListOfMarks(String wordToGuess, String attempt, List<Mark> expectedMarks) {
        AttemptValidator validator = new AttemptValidator();
        assertEquals(expectedMarks, validator.generateMarks(wordToGuess, attempt));
    }

    static Stream<Arguments> provideWordsAndAttempts() {
        return Stream.of(
                Arguments.of(
                        "banaan",
                        "banden",
                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT)),
                Arguments.of(
                        "haren",
                        "heren", //second Mark should be absent, not present because it is already at a correct position
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of(
                        "banana",
                        "banaan",
                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.PRESENT)),
//                  Expected :[CORRECT, CORRECT, CORRECT, CORRECT, ABSENT, ABSENT]
//                  Actual   :[CORRECT, CORRECT, CORRECT, CORRECT, PRESENT, PRESENT]
                Arguments.of(
                        //not words, but for testing if Mark.PRESENT is correctly added to the list.
                        "aaeeae",
                        "eeaaae",
                        List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of(
                        "liniaal",
                        "alianna", //not a word, but for testing if the correct marks are added to the list.
                        List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of(
                        "aaabab",
                        "bbbaaa", //not a word, but for testing if the correct marks are added to the list.
                        List.of(Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT))

        );
    }
}
