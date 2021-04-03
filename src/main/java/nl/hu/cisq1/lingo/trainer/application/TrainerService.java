package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.factory.GameFactory;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class TrainerService {
    private final WordService wordService;
    private final GameRepository gameRepository;
    private final GameFactory gameFactoryInterface;

    public TrainerService(WordService wordService, GameRepository gameRepository, GameFactory gameFactoryInterface) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
        this.gameFactoryInterface = gameFactoryInterface;
    }

    public Game startGame(Long id) {
        if (id == null)
            return startNewGame();
        Optional<Game> existingGame = this.gameRepository.findById(id);
        if (existingGame.isEmpty())
            throw new GameDoesNotExistException("Game with this id does not exist");
        return existingGame.get();
    }

    //Starting a new game always provides a 5 letter word.
    public Game startNewGame() {
        Game game = gameFactoryInterface.createGame();
        game.startNewRound(wordService.provideRandomWord(5));
        this.gameRepository.save(game);
        return game;
    }

    public Game doAttempt(String attempt, Long gameId) {
        Game game = getGameById(gameId);
        if (!game.getActiveRound().isFinished()) {
            wordService.findWordByString(attempt);
            game.doAttempt(attempt);
        }
        this.gameRepository.save(game);
        return game;
    }

    //starts a new round and provides a random word with it's length based on the word of the previous round.
    public Game startNewRound(Long gameId) {
        Game game = getGameById(gameId);
        if (game.getActiveRound().isFinished() && !game.getGameStatus().equals(GameStatus.ELIMINATED))
            game.startNewRound(wordService.provideRandomWord(game.provideLengthNextWordToGuess()));
        this.gameRepository.save(game);
        return game;
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElseThrow(
                () -> new GameDoesNotExistException("Game with this id does not exist")
        );
    }
}
