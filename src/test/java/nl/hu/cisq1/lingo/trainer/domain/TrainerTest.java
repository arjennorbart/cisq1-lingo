package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainerTest {

    private Trainer trainer;

    @BeforeEach
    void initialize() {
        this.trainer = new Trainer();
    }

    @Test
    @DisplayName("If the word is guessed correctly the score should be calculated")
    void calculateScore() {
        trainer.startNewRound("banaan");
        trainer.doAttempt("banana");
        trainer.getActiveRound().setAttempts(3);
        trainer.calculateScore();
        assertEquals(35, trainer.getScore());
    }

    @ParameterizedTest
    @MethodSource("provideRounds")
    @DisplayName("This checks the GameStatus")
    void thisMethodDoesSomething(int tries, String attempt, GameStatus gameStatus) {
        trainer.startNewRound("banaan");
        trainer.getActiveRound().setAttempts(tries);
        trainer.doAttempt(attempt);
        assertEquals(gameStatus, trainer.getGameStatus());
    }

    static Stream<Arguments> provideRounds() {
        return Stream.of(
                Arguments.of(0, "banana", GameStatus.PLAYING),
                Arguments.of(1, "banden", GameStatus.PLAYING),
                Arguments.of(2, "nasaal", GameStatus.PLAYING),
                Arguments.of(3, "banaan", GameStatus.ROUND_WON),
                Arguments.of(4, "banken", GameStatus.ELIMINATED)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWordLength")
    @DisplayName("If the word is guessed correctly")
    void provideLengthNextWordToGuess(int wordLength, String word) {
        trainer.startNewRound(word);
        assertEquals(wordLength, trainer.provideLengthNextWordToGuess());
    }

    static Stream<Arguments> provideWordLength() {
        return Stream.of(
                //should be 6 after a 5 letter word
                Arguments.of(6, "panda"),
                //should be 7 after a 6 letter word
                Arguments.of(7, "banaan"),
                //should be back to 5 after a 7 letter word
                Arguments.of(5, "bananen")
        );
    }
}
