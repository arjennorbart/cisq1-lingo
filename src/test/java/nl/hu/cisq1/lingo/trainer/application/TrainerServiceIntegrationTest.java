package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.TrainerRepository;
import nl.hu.cisq1.lingo.trainer.domain.Trainer;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptLengthException;
import nl.hu.cisq1.lingo.trainer.domain.factory.TrainerFactory;
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
public class TrainerServiceIntegrationTest {

    private Trainer trainer;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerRepository repository;

    @Autowired
    private TrainerFactory trainerFactory;

    @BeforeEach
    void initialize() {
        this.trainer = trainerFactory.createTrainer();
        this.trainer.startNewRound("pizza"); //from WordTestDataFixtures
        this.repository.save(this.trainer);
    }

    @Test
    @DisplayName("New game should not be empty")
    void startingNewGameIsNotNull() {
        assertNotNull(this.trainerService.startGame(null));
    }

    @Test
    @DisplayName("Should throw when attempt is not a word that's in the database")
    void shouldThrowWhenAttemptIsNotACorrectWord() {
        assertThrows(InvalidWordException.class,
                () -> this.trainerService.doAttempt("aaaaa", this.trainer.getId()));
    }

    @Test
    @DisplayName("Should throw when attempt is not the correct length")
    void shouldThrowWhenAttemptLengthIsIncorrect() {
        assertThrows(InvalidAttemptLengthException.class,
                () -> this.trainerService.doAttempt("oranje", this.trainer.getId()));
    }

    @Test
    @DisplayName("Should find a game by id")
    void findGameById() {
        assertNotNull(this.trainerService.getTrainerById(this.trainer.getId()));
    }

    @Test
    @DisplayName("Word is revealed when round is finished")
    void revealWordWhenRoundIsFinished() {
        this.trainer.getActiveRound().setFinished(true);
        assertEquals("pizza",
                this.trainerService.getTrainerById(
                        this.trainer.getId())
                        .getActiveRound()
                        .displayWordToPlayer()
        );
    }
}
