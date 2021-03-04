package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.TrainerData;
import nl.hu.cisq1.lingo.trainer.data.TrainerRepository;
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

    //TODO: should be able to have multiple active games.
    public Trainer startGame() {
        Optional<TrainerData> lastSaved = this.trainerRepository.findTopByOrderByIdDesc();
        if (lastSaved.isEmpty())
            return startNewGame();
        return lastSaved.get().getTrainer();
    }

    //Starting a new game always provides a 5 letter word.
    public Trainer startNewGame() {
        Trainer trainer = trainerFactoryInterface.createTrainer();
        trainer.startNewRound(wordService.provideRandomWord(5));
        TrainerData trainerData = new TrainerData(trainer);
        this.trainerRepository.save(trainerData);
        return trainer;
    }

    //TODO: possible to have multiple active games, so have to get the correct game, not the lastSavedGame.
    public Trainer doAttempt(String attempt) {
        TrainerData lastSavedGame = getLastSavedGame();
        Trainer trainer = lastSavedGame.getTrainer();
        if (!trainer.getActiveRound().isFinished()) {
            wordService.findWordByString(attempt);
            trainer.doAttempt(attempt);
        }
        this.trainerRepository.save(lastSavedGame);
        return trainer;
    }

    //starts a new round and provides a random word with it's length based on the word of the previous round.
    public Trainer startNewRound() {
        TrainerData lastSavedGame = getLastSavedGame();
        Trainer trainer = lastSavedGame.getTrainer();
        if (!trainer.isGameIsFinished() && trainer.getActiveRound().isFinished())
            trainer.startNewRound(wordService.provideRandomWord(trainer.provideLengthNextWordToGuess()));
        this.trainerRepository.save(lastSavedGame);
        return trainer;
    }

    //Need to replace this in the future because there can be multiple active games.
    private TrainerData getLastSavedGame() {
        return this.trainerRepository.findTopByOrderByIdDesc().orElseThrow();
    }
}
