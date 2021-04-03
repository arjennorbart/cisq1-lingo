package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import nl.hu.cisq1.lingo.trainer.domain.factory.GameFactory;
import nl.hu.cisq1.lingo.words.domain.exception.InvalidWordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {

    private Game game;

    @Autowired
    private TrainerService service;

    @Autowired
    private GameRepository repository;

    @Autowired
    private GameFactory gameFactory;

    @BeforeEach
    void initialize() {
        this.game = gameFactory.createGame();
        this.game.startNewRound("pizza"); //from WordTestDataFixtures
        this.repository.save(this.game);
    }

    @Test
    @DisplayName("New game should not be empty")
    void startingNewGameIsNotNull() {
        assertNotNull(this.service.startGame(null));
    }

    @Test
    @DisplayName("Should throw when attempt is not a word that's in the database")
    void shouldThrowWhenAttemptIsNotACorrectWord() {
        Long id = this.game.getId();
        assertThrows(InvalidWordException.class,
                () -> this.service.doAttempt("aaaaa", id));
    }

    @Test
    @DisplayName("Should throw when attempt is not the correct length")
    void shouldThrowWhenAttemptLengthIsIncorrect() {
        Long id = this.game.getId();
        assertThrows(InvalidAttemptLengthException.class,
                () -> this.service.doAttempt("oranje", id));
    }

    @Test
    @DisplayName("Should find a game by id")
    void findGameById() {
        assertNotNull(this.service.getGameById(this.game.getId()));
    }

    @Test
    @DisplayName("Word is revealed when round is finished")
    void revealWordWhenRoundIsFinished() {
        this.game.getActiveRound().setFinished(true);
        assertEquals("pizza",
                this.service.getGameById(
                        this.game.getId())
                        .getActiveRound()
                        .displayWordToPlayer()
        );
    }
}
