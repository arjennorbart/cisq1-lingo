package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Trainer;
import nl.hu.cisq1.lingo.trainer.presentation.dto.AttemptDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.HintDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/startNewGame")
    public HintDTO startTrainer(@RequestParam(required = false) Long id) {
        Trainer trainer = this.trainerService.startGame(id);
        return new HintDTO(
                trainer.getId(),
                trainer.getScore(),
                trainer.getActiveRound().getAttempts(),
                trainer.getActiveRound().getHint(),
                trainer.getActiveRound().getFeedbackList(),
                trainer.getActiveRound().displayWordToPlayer(),
                trainer.getGameStatus()
        );
    }

    @GetMapping("/startNewRound")
    public HintDTO startNewRound(@RequestParam Long gameId) {
        Trainer trainer = this.trainerService.startNewRound(gameId);
        return new HintDTO(
                trainer.getId(),
                trainer.getScore(),
                trainer.getActiveRound().getAttempts(),
                trainer.getActiveRound().getHint(),
                trainer.getActiveRound().getFeedbackList(),
                trainer.getActiveRound().displayWordToPlayer(),
                trainer.getGameStatus()
        );
    }

    @PostMapping("/doAttempt")
    public HintDTO doAttempt(@RequestBody AttemptDTO attempt, @RequestParam Long gameId) {
        Trainer trainer = this.trainerService.doAttempt(attempt.attempt, gameId);
        return new HintDTO(
                trainer.getId(),
                trainer.getScore(),
                trainer.getActiveRound().getAttempts(),
                trainer.getActiveRound().getHint(),
                trainer.getActiveRound().getFeedbackList(),
                trainer.getActiveRound().displayWordToPlayer(),
                trainer.getGameStatus()
        );
    }
}
