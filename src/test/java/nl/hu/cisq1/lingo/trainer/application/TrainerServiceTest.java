package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.TrainerRepository;
import nl.hu.cisq1.lingo.trainer.domain.Trainer;
import nl.hu.cisq1.lingo.trainer.domain.factory.TrainerFactory;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    //stub: standaard implementatie die altijd hetzelfde teruggeeft.
    //fake: repo die gedrag naboost
    //spy: boost gedrag na en geeft telemtry/logging terug. omvat gedrag van simulatie & interactie
    //mock: heeft methoden die er nog op uitgevoerd kunnen worden -> object manipulatie

    @Test
    void test() {
        TrainerRepository mockTrainerRepository = mock(TrainerRepository.class);
        when(mockTrainerRepository.findById(1L)).thenReturn(Optional.of(new Trainer()));

        SpringWordRepository mockWordRepository = mock(SpringWordRepository.class);
        when(mockWordRepository.findRandomWordByLength(6)).thenReturn(Optional.of(new Word("banana")));
        WordService wordService = new WordService(mockWordRepository);

        TrainerFactory trainerFactory = mock(TrainerFactory.class);
        when(trainerFactory.createTrainer()).thenReturn(new Trainer());

        TrainerService service = new TrainerService(wordService, mockTrainerRepository, trainerFactory);
    }
}
