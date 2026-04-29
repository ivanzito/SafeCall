package com.safecall.exceptions;

public class RateLimitExcedeedException extends RuntimeException {
    public RateLimitExcedeedException() {
        super("Rate limit excedeed");
    }

    public RateLimitExcedeedException(Throwable ex) {
        super(ex);
    }
}
