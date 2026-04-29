package com.safecall.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retryable {
    int attempts() default 3;
    long delay() default 1000;
    long timeout() default 1000;

    Class<? extends Throwable>[] exceptions() default {};
}