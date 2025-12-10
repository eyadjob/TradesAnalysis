package com.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Cache configuration for the application.
 * Enables Spring Cache abstraction and configures cache manager and key generator.
 */
@Configuration
@EnableCaching
public class CacheConfiguration implements CachingConfigurer {

    /**
     * Creates a cache manager with named caches.
     * Uses ConcurrentMapCacheManager for in-memory caching.
     *
     * @return CacheManager instance
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // Register cache names - caches will be created dynamically if not listed here
        cacheManager.setCacheNames(Arrays.asList(
                "countryCurrencyInfo",
                "userBranchesForCombobox",
                "countriesForCombobox",
                "currenciesForCombobox",
                "allCarsModelsCache",
                "fuelTypesForCombobox",
                "typesComboboxItems",
                "allItemsComboboxItems",
                "propertiesCache"
        ));
        // Allow dynamic cache creation for any cache name not listed above
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * Creates a custom key generator that automatically generates cache keys
     * based on method name and all parameter values.
     *
     * @return KeyGenerator instance
     */
    @Bean("AutoKeyGenerator")
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            @NonNull
            public Object generate(@NonNull Object target, @NonNull Method method, @NonNull Object... params) {
                // Generate key from method name and all parameters
                String methodName = method.getName();
                String paramsKey = Arrays.stream(params)
                        .map(param -> param != null ? param.toString() : "null")
                        .collect(Collectors.joining("_"));
                
                return methodName + "_" + paramsKey;
            }
        };
    }
}

