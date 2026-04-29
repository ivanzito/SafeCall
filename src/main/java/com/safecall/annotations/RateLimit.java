package com.safecall.annotations;

import com.safecall.core.FixedWindowExecutor;
import com.safecall.core.RateLimitExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int permits() default 10;
    long duration() default 1000;
    Class<? extends  RateLimitExecutor> type() default FixedWindowExecutor.class;
}