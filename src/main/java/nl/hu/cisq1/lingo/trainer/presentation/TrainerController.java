package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.AttemptDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.HintDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService service;

    public TrainerController(TrainerService service) {
        this.service = service;
    }

    @GetMapping("/startNewGame")
    public HintDTO startGame(@RequestParam(required = false) Long id) {
        Game game = this.service.startGame(id);
        return new HintDTO(
                game.getId(),
                game.getScore(),
                game.getActiveRound().getAttempts(),
                game.getActiveRound().getHint(),
                game.getActiveRound().getFeedbackList(),
                game.getActiveRound().displayWordToPlayer(),
                game.getGameStatus()
        );
    }

    @GetMapping("/startNewRound")
    public HintDTO startNewRound(@RequestParam Long gameId) {
        Game game = this.service.startNewRound(gameId);
        return new HintDTO(
                game.getId(),
                game.getScore(),
                game.getActiveRound().getAttempts(),
                game.getActiveRound().getHint(),
                game.getActiveRound().getFeedbackList(),
                game.getActiveRound().displayWordToPlayer(),
                game.getGameStatus()
        );
    }

    @PostMapping("/doAttempt")
    public HintDTO doAttempt(@RequestBody AttemptDTO attempt, @RequestParam Long gameId) {
        Game game = this.service.doAttempt(attempt.attempt, gameId);
        return new HintDTO(
                game.getId(),
                game.getScore(),
                game.getActiveRound().getAttempts(),
                game.getActiveRound().getHint(),
                game.getActiveRound().getFeedbackList(),
                game.getActiveRound().displayWordToPlayer(),
                game.getGameStatus()
        );
    }
}
