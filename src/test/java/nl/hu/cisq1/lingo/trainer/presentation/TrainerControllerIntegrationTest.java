package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.application.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.trainer.data.TrainerRepository;
import nl.hu.cisq1.lingo.trainer.domain.Trainer;
import nl.hu.cisq1.lingo.trainer.domain.factory.TrainerFactory;
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
    private static Long invalidId = 2L;

    private Trainer trainer;
    private AttemptDTO correctAttemptDTO;
    private AttemptDTO incorrectAttemptDTO;
    private TrainerService trainerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainerRepository repository;

    @Autowired
    private TrainerFactory trainerFactory;

    @BeforeEach
    void initialize() {
        this.trainer = trainerFactory.createTrainer();
        this.trainer.startNewRound("pizza"); //from WordTestDataFixtures
        this.repository.save(this.trainer);
        this.trainerService = mock(TrainerService.class);
        this.correctAttemptDTO = new AttemptDTO();
        this.correctAttemptDTO.attempt = "pizza";
        this.incorrectAttemptDTO = new AttemptDTO();
        this.incorrectAttemptDTO.attempt = "incorrect";
        gameId = this.trainer.getId();

        TrainerController trainerController = new TrainerController(this.trainerService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        when(this.trainerService.startGame(gameId)).thenReturn(this.trainer);
        when(this.trainerService.startNewRound(gameId)).thenReturn(this.trainer);
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
        when(this.trainerService.startGame(invalidId)).thenThrow(GameDoesNotExistException.class);
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
        when(this.trainerService.startNewRound(invalidId)).thenThrow(GameDoesNotExistException.class);
        this.performBadRequest(requestBuilder);
    }

    @Test
    @DisplayName("Attempt is correct")
    void attemptIsCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/trainer/doAttempt?gameId=" + gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(correctAttemptDTO));
        when(this.trainerService.doAttempt(correctAttemptDTO.attempt, gameId)).thenReturn(this.trainer);
        this.performSuccessfulRequest(requestBuilder);
    }

    @Test
    @DisplayName("Attempt is not correct")
    void attemptIsIncorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/trainer/doAttempt?gameId=" + gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.incorrectAttemptDTO));
        when(this.trainerService.doAttempt(incorrectAttemptDTO.attempt, gameId)).thenThrow(InvalidWordException.class);
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
