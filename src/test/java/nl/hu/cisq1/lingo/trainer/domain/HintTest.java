package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HintTest {

    @Test
    @DisplayName("Previous hint is correctly replaced for a new hint")
    void replaceHintasdf() {
        Hint previousHint = new Hint(List.of('b', 'a', '.', '.', '.', '.'));
        previousHint.replaceHint(List.of('.', '.', '.', '.', '.', 'n'));
        assertEquals(previousHint, new Hint(List.of('b', 'a', '.', '.', '.', 'n')));
    }

    @ParameterizedTest
    @DisplayName("Previous hint is correctly replaced for a new hint")
    @MethodSource("provideHintExamples")
    void replaceHint(Hint previousHint, Hint combinedHint, List<Character> attempt) {
        previousHint.replaceHint(attempt);
        //previous hint is now combined with the attempt
        assertEquals(previousHint, combinedHint);
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(
                        new Hint(List.of('.', '.', '.', '.', '.', 'a')),
                        new Hint(List.of('b', '.', '.', '.', '.', 'a')),
                        List.of('b', '.', '.', '.', '.', 'a')),
                Arguments.of(
                        //previous hint is "katoen"
                        new Hint(List.of('.', 'a', '.', '.', '.', 'n')),
                        //if the attempt is "kanaal" the feedback should be ".anaan" and not ".anaa."
                        new Hint(List.of('.', 'a', 'n', 'a', 'a', 'n')),
                        List.of('.', 'a', 'n', 'a', 'a', 'l')),
                Arguments.of(
                        //previous hint is "katoen"
                        new Hint(List.of('.', 'a', '.', '.', '.', 'n')),
                        //if the attempt is "katana" the feedback should be ".a.a+n" and not ".anaa."
                        new Hint(List.of('.', 'a', '.', 'a', '+', 'n')),
                        List.of('.', 'a', '.', 'a', '+', '+'))
        );
    }
}
