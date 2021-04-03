package nl.hu.cisq1.lingo.trainer.domain.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConcreteGameFactoryTest {

    @Test
    @DisplayName("returns new Game")
    void returnNewGame() {
        ConcreteGameFactory concreteGameFactory = new ConcreteGameFactory();
        assertNotNull(concreteGameFactory.createGame());
    }
}