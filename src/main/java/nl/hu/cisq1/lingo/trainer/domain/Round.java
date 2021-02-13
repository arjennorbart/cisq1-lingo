package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Round {
    private String wordToGuess;
    private List<String> attempts;
    private Feedback feedback;

    public Round(String wordToGuess, List<String> attempts) {
        this.wordToGuess = wordToGuess;
        this.attempts = attempts;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public void setWordToGuess(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    public List<String> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<String> attempts) {
        this.attempts = attempts;
    }
}
