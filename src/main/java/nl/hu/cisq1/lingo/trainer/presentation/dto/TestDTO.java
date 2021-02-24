package nl.hu.cisq1.lingo.trainer.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Round;

@AllArgsConstructor
@Getter
public class TestDTO {
    public final Round round;
    public final GameStatus gameStatus;
}
