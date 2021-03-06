package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    private Game game;

    @BeforeEach
    void initialize() {
        this.game = new Game();
    }

    @ParameterizedTest
    @MethodSource("provideScoreAndStatus")
    @DisplayName("tests if the correct score is applied")
    void scoreTest(Boolean isFinished, GameStatus gameStatus, Integer score, Integer tries) {
        game.startNewRound("banaan");
        game.getActiveRound().setAttempts(tries);
        game.getActiveRound().setFinished(isFinished);
        game.checkGameStatus(gameStatus);
        assertEquals(score, game.getScore());
    }

    static Stream<Arguments> provideScoreAndStatus() {
        return Stream.of(
                Arguments.of(false, GameStatus.PLAYING, 0, 0),
                Arguments.of(true, GameStatus.ELIMINATED, 0, 0),
                Arguments.of(true, GameStatus.ROUND_WON, 35, 3),
                Arguments.of(true, GameStatus.ROUND_WON, 40, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRounds")
    @DisplayName("This checks the GameStatus")
    void checkGameStatus(int tries, String attempt, GameStatus gameStatus) {
        game.startNewRound("banaan");
        game.getActiveRound().setAttempts(tries);
        game.doAttempt(attempt);
        assertEquals(gameStatus, game.getGameStatus());
    }

    static Stream<Arguments> provideRounds() {
        return Stream.of(
                Arguments.of(5, "banaan", GameStatus.ROUND_WON),
                Arguments.of(1, "banana", GameStatus.PLAYING),
                Arguments.of(2, "banden", GameStatus.PLAYING),
                Arguments.of(3, "nasaal", GameStatus.PLAYING),
                Arguments.of(4, "banaan", GameStatus.ROUND_WON),
                Arguments.of(5, "banken", GameStatus.ELIMINATED)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWordLength")
    @DisplayName("If the word is guessed correctly")
    void provideLengthNextWordToGuess(int wordLength, String word) {
        game.startNewRound(word);
        assertEquals(wordLength, game.provideLengthNextWordToGuess());
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
