package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.TrainerRepository;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Trainer;
import nl.hu.cisq1.lingo.trainer.domain.factory.TrainerFactory;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class TrainerService {
    private final WordService wordService;
    private final TrainerRepository trainerRepository;
    private final TrainerFactory trainerFactoryInterface;

    public TrainerService(WordService wordService, TrainerRepository trainerRepository, TrainerFactory trainerFactoryInterface) {
        this.wordService = wordService;
        this.trainerRepository = trainerRepository;
        this.trainerFactoryInterface = trainerFactoryInterface;
    }

    public Trainer startGame(Long id) {
        if (id == null)
            return startNewGame();
        Optional<Trainer> existingGame = this.trainerRepository.findById(id);
        if (existingGame.isEmpty())
            return startNewGame();
        return existingGame.get();
    }

    //Starting a new game always provides a 5 letter word.
    public Trainer startNewGame() {
        Trainer trainer = trainerFactoryInterface.createTrainer();
        trainer.startNewRound(wordService.provideRandomWord(5));
        this.trainerRepository.save(trainer);
        return trainer;
    }

    public Trainer doAttempt(String attempt, Long gameId) {
        Trainer game = getTrainerById(gameId);
        if (!game.getActiveRound().isFinished()) {
            wordService.findWordByString(attempt);
            game.doAttempt(attempt);
        }
        this.trainerRepository.save(game);
        return game;
    }

    //starts a new round and provides a random word with it's length based on the word of the previous round.
    public Trainer startNewRound(Long gameId) {
        Trainer game = getTrainerById(gameId);
        if (game.getActiveRound().isFinished() && !game.getGameStatus().equals(GameStatus.ELIMINATED))
            game.startNewRound(wordService.provideRandomWord(game.provideLengthNextWordToGuess()));
        this.trainerRepository.save(game);
        return game;
    }

    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id).orElseThrow();
    }
}
