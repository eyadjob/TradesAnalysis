package com.controllers;

import com.beans.general.AbpResponseBean;
import com.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * REST controller for dashboard-related operations.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get booking for quick search by booking number.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param bookingNo The booking number to search for (required, e.g., I25000U1024054046).
     * @return The response containing booking information for quick search.
     */
    @GetMapping(path = BOOKING_GET_BOOKING_FOR_QUICK_SEARCH, produces = "application/json")
    public AbpResponseBean getBookingForQuickSearch(
            @RequestParam(required = true) String bookingNo) {

        if (bookingNo == null || bookingNo.isEmpty()) {
            throw new IllegalArgumentException("bookingNo parameter is required.");
        }

        return dashboardService.getBookingForQuickSearch(bookingNo);
    }
}

