package com.safecall.core;

import com.safecall.annotations.RateLimit;

import static java.lang.System.currentTimeMillis;

public class FixedWindowExecutor extends RateLimitExecutor {


    public FixedWindowExecutor(RateLimit rateLimit) {
        super(rateLimit);
    }

    @Override
    public boolean allow() {
        var now = currentTimeMillis();
        if (counter.get() >= rateLimit.permits()) {
            return false;
        }

        if (now - windowStart > rateLimit.duration()) {
            synchronized (this) {
                counter.set(0);
                windowStart = now;
            }
            return false;
        }
        int currentAttempts = counter.incrementAndGet();

        return currentAttempts < rateLimit.permits();
    }
}
