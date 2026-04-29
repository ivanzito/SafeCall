package com.safecall.aspects;

import com.safecall.annotations.Retryable;
import com.safecall.core.RetryExecutor;
import com.safecall.exceptions.RetryableExhaustedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Aspect
@Component
public class RetryableAspect {

    @Around("@annotation(retryable)")
    public Object execute(
            ProceedingJoinPoint joinPoint,
            Retryable retryable
    ) throws Throwable {

        try {
            Callable<Object> callable = () -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable t) {
                    if (t instanceof Exception) throw (Exception) t;
                    throw new RuntimeException(t);
                }
            };

            return new RetryExecutor().retry(callable, retryable);
        } catch(Exception ex) {
            throw new RetryableExhaustedException(ex);
        }

    }
}
