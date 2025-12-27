package com.services;

import com.annotation.LogExecutionTime;
import com.beans.general.AbpResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for interacting with dashboard-related APIs.
 */
@Service
public class DashboardService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Get booking for quick search by booking number.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param bookingNo The booking number to search for (required, e.g., I25000U1024054046).
     * @return The response containing booking information for quick search.
     */
    @Cacheable(cacheNames = "getBookingForQuickSearchCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public AbpResponseBean getBookingForQuickSearch(String bookingNo) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/GetBookingForQuickSearch")
                        .queryParam("bookingNo", bookingNo)
                        .build())
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }
}

