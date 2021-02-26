package nl.hu.cisq1.lingo.trainer.domain.factory;

import nl.hu.cisq1.lingo.trainer.domain.Trainer;
import org.springframework.stereotype.Component;

@Component
public class ConcreteTrainerFactory implements TrainerFactory{

    @Override
    public Trainer createTrainer() {
        return new Trainer();
    }
}
