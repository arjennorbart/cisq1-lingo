package nl.hu.cisq1.lingo.trainer.domain.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteTrainerFactoryTest {

    @Test
    @DisplayName("returns new Trainer")
    void returnNewTrainer() {
        ConcreteTrainerFactory concreteTrainerFactory = new ConcreteTrainerFactory();
        assertNotNull(concreteTrainerFactory.createTrainer());
    }
}