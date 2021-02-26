package nl.hu.cisq1.lingo.trainer.domain.exception;

public class ReachedMaxAttemptsException extends RuntimeException {
    public ReachedMaxAttemptsException() {
    }

    public ReachedMaxAttemptsException(String message) {
        super(message);
    }
}
