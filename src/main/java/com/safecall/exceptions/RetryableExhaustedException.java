package com.safecall.exceptions;

public class RetryableExhaustedException extends Throwable {
    public RetryableExhaustedException(Throwable ex) {
        super(ex);
    }

    public RetryableExhaustedException(String message, Throwable ex) {
        super(message, ex);
    }
}
