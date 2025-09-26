package ru.giv13.mailer.exception;

public class NonRetryableException extends RuntimeException {
    public NonRetryableException(Throwable cause) {
        super(cause);
    }
}
