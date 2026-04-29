package com.safecall.core;

import com.safecall.annotations.RateLimit;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitRegistry {

    private final ConcurrentHashMap<String, RateLimitExecutor> registry = new ConcurrentHashMap<>();

    public RateLimitExecutor getOrCreate(String key, RateLimit config) {

        RateLimitExecutor hasRateLimit = registry.get(key);
        registry.computeIfAbsent(key, k -> {

            try {
                return config.type()
                        .getConstructor(RateLimit.class)
                        .newInstance(config);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Erro ao criar executor: " + config.type().getName(), e);
            }
        });

        if (hasRateLimit == null) {
            return registry.put(key, new FixedWindowExecutor(config));
        }

        return registry.get(key);
    }

}
