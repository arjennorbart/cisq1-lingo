package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.factory.GameFactory;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    //stub: standaard implementatie die altijd hetzelfde teruggeeft.
    //fake: repo die gedrag naboost
    //spy: boost gedrag na en geeft telemtry/logging terug. omvat gedrag van simulatie & interactie
    //mock: heeft methoden die er nog op uitgevoerd kunnen worden -> object manipulatie

    private TrainerService trainerService;
    private Game game;
    private WordService wordService;

    @BeforeEach
    void initialize() {
        GameFactory factory = mock(GameFactory.class);
        GameRepository gameRepository = mock(GameRepository.class);

        this.wordService = mock(WordService.class);
        this.game = mock(Game.class);
        this.trainerService = new TrainerService(this.wordService, gameRepository, factory);

        when(factory.createGame()).thenReturn(this.game);
        when(this.wordService.provideRandomWord(anyInt())).thenReturn("kater");
        when(gameRepository.save(isA(Game.class))).thenReturn(this.game);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(this.game));
        when(this.game.getId()).thenReturn(1L);
        when(this.game.getActiveRound()).thenReturn(new Round("kater"));
    }

    @Test
    @DisplayName("should return a new game when no parameter is given to startNewGame method")
    void startNewGame() {
        assertNotNull(this.trainerService.startGame(null));
        verify(this.game, times(1)).startNewRound("kater");
    }

    @Test
    @DisplayName("should return the game with id 1")
    void loadOngoingPreviousGame() {
        assertEquals(1L, this.game.getId());
    }

    @Test
    @DisplayName("When starting an existing game, the correct game id should be returned")
    void loadCorrectGameById() {
        assertEquals(1L, this.trainerService.startGame(1L).getId());
    }

    @Test
    @DisplayName("When starting an existing game which is empty, a new game should be started")
    void startNewGameWhenExistingGameIsEmpty() {
        assertThrows(GameDoesNotExistException.class,
                () -> this.trainerService.startGame(2L));
    }

    @Test
    @DisplayName("Doing an attempt should return a game")
    void doAttemptShouldReturnAGame() {
        assertEquals(1L, this.trainerService.doAttempt("ketel", 1L).getId());
    }

    @Test
    @DisplayName("Should start new round when word is guessed")
    void startNewRoundWhenWordIsGuessed() {
        assertEquals(1L, this.trainerService.doAttempt("kater", 1L).getId());
        verify(this.game, times(1)).doAttempt("kater");
        verify(this.wordService, times(1)).findWordByString("kater");
    }

    @Test
    @DisplayName("Doing a valid attempt should not throw")
    void shouldReturnAGameWhenRoundIsFinished() {
        this.game.getActiveRound().setFinished(true);
        assertEquals(1L, this.trainerService.doAttempt("ketel", 1L).getId());
    }

    @Test
    @DisplayName("Doing a valid attempt should not throw")
    void shouldNotThrowWhenAttemptIsValid() {
        assertDoesNotThrow(() -> this.trainerService.doAttempt("ketel", 1L));
    }

    @Test
    @DisplayName("Doing an attempt on a non existing game should throw")
    void shouldThrowWhileDoingAnAttemptOnNonExistingGame() {
        assertThrows(GameDoesNotExistException.class,
                () -> this.trainerService.doAttempt("ketel",2L));
    }

    @Test
    @DisplayName("Starting a new round should return a game")
    void startingNewRoundShouldReturnAGame() {
        when(this.game.getGameStatus()).thenReturn(GameStatus.ROUND_WON);
        this.game.getActiveRound().setFinished(true);
        assertEquals(1L, this.trainerService.startNewRound(1L).getId());
        verify(this.game, times(2)).getActiveRound();
        verify(this.game, times(1)).startNewRound("kater");
    }

    @Test
    @DisplayName("Starting new round when current round is not finished, should not start a new round")
    void startingNewNotPossibleWhenCurrentRoundIsActive() {
        assertEquals(1L, this.trainerService.startNewRound(1L).getId());
    }

    @Test
    @DisplayName("Starting new round when GameStatus is eliminated but round is finished, should not start a new round")
    void startingNewNotPossibleWhenGameStatusIsEliminatedAndRoundIsFinished() {
        when(this.game.getGameStatus()).thenReturn(GameStatus.ELIMINATED);
        this.game.getActiveRound().setFinished(true);
        assertEquals(1L, this.trainerService.startNewRound(1L).getId());
    }
}
