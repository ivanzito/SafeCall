package com.safecall.core;

import com.safecall.annotations.Retryable;
import com.safecall.exceptions.RetryableExhaustedException;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import static java.lang.System.currentTimeMillis;

public class RetryExecutor {

    public <T> T retry(Callable<T> callable, Retryable retryable) throws Throwable {
        Throwable lastException = null;
        for (int i = 0; i < retryable.attempts(); i++) {
            try {
                return callable.call();
            } catch (Throwable ex) {
                lastException = (ex.getCause() != null) ? ex.getCause() : ex;
                long startTime = currentTimeMillis();
                handleException(retryable, ex, i, startTime);
            }
        }

        throw new RetryableExhaustedException("Max attempts reached", lastException);
    }

    private void handleException(Retryable retryable, Throwable ex, int index, long startTime) throws Throwable {

        if (retryable.timeout() > 0) {
            handleTimeout(retryable, startTime);
        }

        boolean shouldRetry = Arrays
                .stream(retryable.exceptions())
                .anyMatch(clazz -> clazz.isAssignableFrom(ex.getClass()));
        if (!shouldRetry) {

            throw ex;
        }

        if (index < retryable.attempts() - 1 && retryable.delay() > 0) {
            backoff(retryable, index);
        }
    }

    private void handleTimeout(Retryable retryable, long initialTime) throws Throwable {

        long elapsed = currentTimeMillis();
        if (elapsed - initialTime > retryable.timeout()) {
            throw new TimeoutException("The request was interrupted due timeout of Retryable annotation");
        }
    }

    private void backoff(Retryable retryable, int index) throws InterruptedException {
        long delay = (long) (retryable.delay() * Math.pow(2, index));
        long jitter = ThreadLocalRandom.current().nextLong(0, Math.max(1, delay / 10));
        Thread.sleep(delay + jitter);
    }

}
