package com.services;

import com.annotation.LogExecutionTime;
import com.beans.booking.GetCreateBookingDateInputsResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for interacting with booking-related APIs.
 */
@Service
public class BookingService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    /**
     * Get create booking date inputs.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the booking date inputs (required).
     * @return The response containing create booking date inputs.
     */
    @Cacheable(cacheNames = "createBookingDateInputsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCreateBookingDateInputsResponseBean getCreateBookingDateInputs(Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/GetCreateBookingDateInputs")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(GetCreateBookingDateInputsResponseBean.class)
                .block();
    }
}

