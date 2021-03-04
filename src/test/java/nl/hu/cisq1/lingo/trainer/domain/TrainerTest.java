package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainerTest {

    @Test
    @DisplayName("If the word is guessed correctly the score should be calculated")
    void calculateScore() {
        Trainer trainer = new Trainer();
        trainer.startNewRound("banaan");
        trainer.doAttempt("banana");
        trainer.getActiveRound().setAttempts(3);
        trainer.calculateScore();
        assertEquals(35, trainer.getScore());
    }

    @Test
    @DisplayName("If the word is guessed correctly the gamestatus should be round won")
    void gameStatusRoundWonWhenWordIsGuessed() {
        Trainer trainer = new Trainer();
        trainer.startNewRound("banaan");
        trainer.getActiveRound().setWordIsGuessed(true);
        trainer.checkGameStatus();
        assertEquals(GameStatus.ROUND_WON, trainer.getGameStatus());
    }

    @Test
    @DisplayName("If the word is guessed correctly the gamestatus should be round won")
    void gameStatusEliminatedWhenWordIsNotGuessedWithin5Attempts() {
        Trainer trainer = new Trainer();
        trainer.startNewRound("banaan");
        trainer.getActiveRound().setAttempts(5);
        trainer.getActiveRound().setWordIsGuessed(false);
        trainer.checkGameStatus();
        assertEquals(GameStatus.ELIMINATED, trainer.getGameStatus());
    }


    @Test
    @DisplayName("gamestatus should be playing when starting a new round")
    void gameStatusPlayingWhenStartingNewRound() {
        Trainer trainer = new Trainer();
        trainer.startNewRound("banaan");
        assertEquals(GameStatus.PLAYING, trainer.getGameStatus());
    }

    @Test
    @DisplayName("When a round is finished it should be added to the rounds list")
    void roundShouldBeAddedToListAfterFinishing() {
        Trainer trainer = new Trainer();
        trainer.startNewRound("banaan");
        trainer.getActiveRound().setWordIsGuessed(true);
        trainer.checkGameStatus();
        List<Round> rounds = new ArrayList<>();
        rounds.add(trainer.getActiveRound());
        assertEquals(rounds, trainer.getPreviousRounds());
    }

    @ParameterizedTest
    @MethodSource("provideWordLength")
    @DisplayName("If the word is guessed correctly")
    void provideLengthNextWordToGuess(int wordLength, String word) {
        Trainer trainer = new Trainer();
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
