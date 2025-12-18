package com.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.time.Duration;
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
     * Uses CaffeineCacheManager for in-memory caching with 2-hour expiration.
     * All methods annotated with @Cacheable will have their data cached for 2 hours.
     *
     * @return CacheManager instance
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configure Caffeine cache with 2-hour expiration
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(2))
                .maximumSize(10_000) // Maximum number of entries in cache
                .recordStats()); // Enable cache statistics
        
        // Register cache names - caches will be created dynamically if not listed here
        cacheManager.setCacheNames(Arrays.asList(
                "countryCurrencyInfo",
                "userBranchesForCombobox",
                "branchesCountriesComboboxItemsCache",
                "countriesForCombobox",
                "currenciesForCombobox",
                "nationalitiesForComboboxCache",
                "allCarsModelsCache",
                "fuelTypesForCombobox",
                "typesComboboxItems",
                "allItemsComboboxItems",
                "propertiesCache",
                "allInsuranceCompaniesCache",
                "allAccidentPoliciesCache",
                "vendorComboboxCache",
                "updateAllSettingsCache",
                "changeTenantSettingsCache",
                "updateCountrySettingsCache",
                "changeBranchSettingsCache",
                "operationalCountriesCache",
                "countriesPhoneCache",
                "allPermissionsCache",
                "createOrUpdateRoleCache",
                "createOrUpdateCustomerCache",
                "createVehiclesCache",
                "allBranchVehiclesCache",
                "vehicleCheckPreparationDataCache",
                "uploadBase64FileCachedData",
                "extrasNamesExcludedFromBookingPaymentDetailsCache",
                "createBookingDateInputsCache",
                "isValidPhoneCache",
                "customerContractInformationByNameCache",
                "itemsByTypeCache",
                "allExternalLoyaltiesConfigurationsItemsCache",
                "integratedLoyaltiesCache",
                "externalLoyaltiesWithAllowRedeemComboboxCache",
                "contractExtraItemsCache",
                "paymentMethodsComboboxItemsCache",
                "bestRentalRateForModelCache",
                "openContractDateInputsCache",
                "authenticationCache",
                "countrySettingsCache",
                "branchAvailableModelsForBookingComboboxItemsCache",
                "integratedLoyaltiesFromLoyaltyApiCache",
                "externalLoyaltiesConfigurationsItemsFromLoyaltyApiCache",
                "externalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApiCache",
                "allRentalRatesSchemasCache",
                "branchSettingsCache"
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

