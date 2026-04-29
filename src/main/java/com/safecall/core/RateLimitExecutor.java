package com.safecall.core;

import com.safecall.annotations.RateLimit;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.currentTimeMillis;

public abstract class RateLimitExecutor {

    RateLimit rateLimit;
    long windowStart = 0;
    AtomicInteger counter;

    public RateLimitExecutor(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
        this.windowStart = currentTimeMillis();
        this.counter = new AtomicInteger();
    }

    public abstract boolean allow();
}
