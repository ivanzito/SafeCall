package com.safecall.core;

import com.safecall.annotations.RateLimit;

import java.util.Deque;
import java.util.LinkedList;

import static java.lang.System.currentTimeMillis;

public class SlidingWindowExecutor extends RateLimitExecutor {

    private final Deque<Long> timestamps = new LinkedList<>();

    public SlidingWindowExecutor(RateLimit rateLimit) {
        super(rateLimit);
    }

    @Override
    public synchronized boolean allow() {
        var now = currentTimeMillis();
        long windowStart = now - rateLimit.duration();

        while (true) {
            Long oldest = timestamps.peek();
            if (oldest != null && timestamps.peekFirst() < windowStart) {
                if(timestamps.removeFirstOccurrence(oldest)) {
                    counter.decrementAndGet();
                }
            } else {
                break;
            }
        }

        if (timestamps.size() < rateLimit.permits()) {
            timestamps.addLast(now);
            return true;
        }

        return false;
    }
}
