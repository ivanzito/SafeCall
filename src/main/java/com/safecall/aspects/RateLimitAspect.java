package com.safecall.aspects;

import com.safecall.annotations.RateLimit;
import com.safecall.core.RateLimitExecutor;
import com.safecall.core.RateLimitRegistry;
import com.safecall.exceptions.RateLimitExcedeedException;
import com.safecall.exceptions.RetryableExhaustedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RateLimitAspect {


    private final RateLimitRegistry registry = new RateLimitRegistry();

    @Around("@annotation(rateLimit)")
    public Object execute(
            ProceedingJoinPoint joinPoint,
            RateLimit rateLimit
    ) throws Throwable {


        String key = joinPoint.getSignature().toLongString();

        RateLimitExecutor rateLimiter = registry.getOrCreate(key, rateLimit);
        try {
            if (rateLimiter.allow()) {
                return joinPoint.proceed();
            } else {
                throw new RateLimitExcedeedException();
            }
        } catch(Exception ex) {
            throw new RateLimitExcedeedException(ex);
        }
    }
}
