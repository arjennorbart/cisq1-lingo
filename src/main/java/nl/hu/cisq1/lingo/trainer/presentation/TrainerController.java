package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
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

    @GetMapping("/start")
    public HintDTO startTrainer() {
        Trainer trainer = this.trainerService.startGame();
        return new HintDTO(trainer.getActiveRound().getHint(), trainer.getGameStatus());
    }

    @PostMapping("/doAttempt")
    public HintDTO doAttempt(@RequestBody AttemptDTO attempt) {
        Trainer trainer = this.trainerService.doAttempt(attempt.attempt);
        return new HintDTO(trainer.getActiveRound().getHint(), trainer.getGameStatus());
    }
}
