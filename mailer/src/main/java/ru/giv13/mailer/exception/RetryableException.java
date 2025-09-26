package ru.giv13.mailer.exception;

public class RetryableException extends RuntimeException {
    public RetryableException(Throwable cause) {
        super(cause);
    }
}
