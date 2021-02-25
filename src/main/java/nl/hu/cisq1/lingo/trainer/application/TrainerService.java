package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.TrainerData;
import nl.hu.cisq1.lingo.trainer.data.TrainerRepository;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
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

    public Trainer startGame() {
        Optional<TrainerData> lastSaved = this.trainerRepository.findTopByOrderByIdDesc();
        if (lastSaved.isEmpty())
            return startNewGame();
        Trainer trainer = lastSaved.get().getTrainer();
        if (!trainer.isFinished())
            return trainer;
        return null;
    }

    public Trainer startNewGame() {
        Trainer trainer = trainerFactoryInterface.createTrainer();
        String randomFirstWord = wordService.provideRandomWord(5);
        trainer.startNewRound(randomFirstWord);
        TrainerData trainerData = new TrainerData(trainer);
        this.trainerRepository.save(trainerData);
        return trainer;
    }

    public Trainer doAttempt(String attempt) {
        TrainerData lastSavedGame = getLastSavedGame();
        Trainer trainer = lastSavedGame.getTrainer();
        trainer.doAttempt(attempt);
        this.trainerRepository.save(lastSavedGame);
        return trainer;
    }

    private TrainerData getLastSavedGame() {
        return this.trainerRepository.findTopByOrderByIdDesc().orElseThrow();
    }
}
