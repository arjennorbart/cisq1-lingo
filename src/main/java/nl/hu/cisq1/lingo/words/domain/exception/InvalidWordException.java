package nl.hu.cisq1.lingo.words.domain.exception;

public class InvalidWordException extends RuntimeException {
    public InvalidWordException() {
    }

    public InvalidWordException(String message) {
        super(message);
    }
}
