package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Round {
    private String wordToGuess;
    private int attempts;
    private Feedback feedback;

    public Round(String wordToGuess, int attempts) {
        this.wordToGuess = wordToGuess;
        this.attempts = attempts;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public void setWordToGuess(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
