package com.controllers;

import com.beans.booking.GetCreateBookingDateInputsResponseBean;
import com.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * REST controller for booking-related operations.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Get create booking date inputs.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The country ID for which to get the booking date inputs (required).
     * @return The response containing create booking date inputs.
     */
    @GetMapping(path = BOOKING_GET_CREATE_BOOKING_DATE_INPUTS, produces = "application/json")
    public GetCreateBookingDateInputsResponseBean getCreateBookingDateInputs(
            @RequestParam(required = true) Integer countryId) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return bookingService.getCreateBookingDateInputs(countryId);
    }
}

