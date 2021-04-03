package nl.hu.cisq1.lingo.trainer.domain.factory;

import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.springframework.stereotype.Component;

@Component
public class ConcreteGameFactory implements GameFactory {

    @Override
    public Game createGame() {
        return new Game();
    }
}
