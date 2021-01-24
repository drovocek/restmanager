package edu.volkov.restmanager.util.exception;

public class NotInTimeLimitException extends RuntimeException {
    public NotInTimeLimitException(String message) {
        super(message);
    }
}