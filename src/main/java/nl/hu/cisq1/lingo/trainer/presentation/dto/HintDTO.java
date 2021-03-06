package nl.hu.cisq1.lingo.trainer.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Hint;

import java.util.List;

@AllArgsConstructor
@Getter
public class HintDTO {
    public final Long gameId;
    public final int score;
    public final int attempts;
    public final Hint hint;
    public final List<Feedback> feedbackList;
    public final String wordToGuess;
    public final GameStatus gameStatus;
}
