package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.application.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.factory.GameFactory;
import nl.hu.cisq1.lingo.trainer.presentation.dto.AttemptDTO;
import nl.hu.cisq1.lingo.words.domain.exception.InvalidWordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(CiTestConfiguration.class)
class TrainerControllerIntegrationTest {
    private static Long gameId;
    private static final Long invalidId = 2L;

    private Game game;
    private AttemptDTO correctAttemptDTO;
    private AttemptDTO incorrectAttemptDTO;
    private TrainerService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository repository;

    @Autowired
    private GameFactory gameFactory;

    @BeforeEach
    void initialize() {
        this.game = gameFactory.createGame();
        this.game.startNewRound("pizza"); //from WordTestDataFixtures
        this.repository.save(this.game);
        this.service = mock(TrainerService.class);
        this.correctAttemptDTO = new AttemptDTO();
        this.correctAttemptDTO.attempt = "pizza";
        this.incorrectAttemptDTO = new AttemptDTO();
        this.incorrectAttemptDTO.attempt = "incorrect";
        gameId = this.game.getId();

        TrainerController trainerController = new TrainerController(this.service);
        this.mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        when(this.service.startGame(gameId)).thenReturn(this.game);
        when(this.service.startNewRound(gameId)).thenReturn(this.game);
    }

    @Test
    @DisplayName("Starting a new game should succeed")
    void startNewGameShouldBeSuccessful() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trainer/startNewGame?id=" + gameId);
        this.performSuccessfulRequest(requestBuilder);
    }

    @Test
    @DisplayName("Starting a game with non existing id should be bad request")
    void startNewGameWithNonExistingIdShouldFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trainer/startNewGame?id=" + invalidId);
        when(this.service.startGame(invalidId)).thenThrow(GameDoesNotExistException.class);
        this.performBadRequest(requestBuilder);
    }

    @Test
    @DisplayName("Starting new round successfully")
    void startingNewRoundSuccessfully() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trainer/startNewRound?gameId=" + gameId);
        this.performSuccessfulRequest(requestBuilder);
    }

    @Test
    @DisplayName("Starting a round with non existing id should be bad request")
    void startNewRoundWithNonExistingIdShouldFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trainer/startNewRound?gameId=" + invalidId);
        when(this.service.startNewRound(invalidId)).thenThrow(GameDoesNotExistException.class);
        this.performBadRequest(requestBuilder);
    }

    @Test
    @DisplayName("Attempt is correct")
    void attemptIsCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/trainer/doAttempt?gameId=" + gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(correctAttemptDTO));
        when(this.service.doAttempt(correctAttemptDTO.attempt, gameId)).thenReturn(this.game);
        this.performSuccessfulRequest(requestBuilder);
    }

    @Test
    @DisplayName("Attempt is not correct")
    void attemptIsIncorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/trainer/doAttempt?gameId=" + gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.incorrectAttemptDTO));
        when(this.service.doAttempt(incorrectAttemptDTO.attempt, gameId)).thenThrow(InvalidWordException.class);
        this.performBadRequest(requestBuilder);
    }

    private void performSuccessfulRequest(RequestBuilder requestBuilder) throws Exception {
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private void performBadRequest(RequestBuilder requestBuilder) throws Exception {
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
