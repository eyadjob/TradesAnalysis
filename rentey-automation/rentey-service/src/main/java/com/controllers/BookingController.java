package com.controllers;

import com.beans.booking.GetCreateBookingDateInputsResponseBean;
import com.beans.customer.GetCustomerContractInformationByNameResponseBean;
import com.beans.loyalty.GetAllExternalLoyaltiesConfigurationsItemsResponseBean;
import com.beans.loyalty.GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean;
import com.beans.loyalty.GetIntegratedLoyaltiesResponseBean;
import com.beans.validation.IsValidPhoneResponseBean;
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

    /**
     * Validate phone number.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * Note: This endpoint uses BASE_PATH_WITHOUT_SERVICE since the API is at /api/ValidatePhone/IsValid
     * (not under /api/services/app).
     *
     * @param phoneNumber The phone number to validate (required).
     * @param phoneCode The phone code (country code) to validate (required).
     * @return The response containing the validation result (true if valid, false otherwise).
     */
    @GetMapping(path = BASE_PATH_WITHOUT_SERVICE + VALIDATE_PHONE_IS_VALID, produces = "application/json")
    public IsValidPhoneResponseBean isValidPhone(
            @RequestParam(required = true) String phoneNumber,
            @RequestParam(required = true) String phoneCode) {

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("phoneNumber parameter is required.");
        }
        if (phoneCode == null || phoneCode.isEmpty()) {
            throw new IllegalArgumentException("phoneCode parameter is required.");
        }

        return bookingService.isValidPhone(phoneNumber, phoneCode);
    }

    /**
     * Get customer contract information by name.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param customerName The customer name to search for (required).
     * @return The response containing customer contract information matching the name.
     */
    @GetMapping(path = CUSTOMER_GET_CONTRACT_INFORMATION_BY_NAME, produces = "application/json")
    public GetCustomerContractInformationByNameResponseBean getCustomerContractInformationByName(
            @RequestParam(required = true) String customerName) {

        if (customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException("customerName parameter is required.");
        }

        return bookingService.getCustomerContractInformationByName(customerName);
    }

    /**
     * Get all external loyalties configurations items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing all external loyalties configurations items.
     */
    @GetMapping(path = EXTERNAL_LOYALTY_CONFIGURATION_GET_ALL_ITEMS, produces = "application/json")
    public GetAllExternalLoyaltiesConfigurationsItemsResponseBean getAllExternalLoyaltiesConfigurationsItems(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive) {
        return bookingService.getAllExternalLoyaltiesConfigurationsItems(includeInActive);
    }

    /**
     * Get integrated loyalties.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all integrated loyalties.
     */
    @GetMapping(path = EXTERNAL_LOYALTY_CONFIGURATION_GET_INTEGRATED_LOYALTIES, produces = "application/json")
    public GetIntegratedLoyaltiesResponseBean getIntegratedLoyalties() {
        return bookingService.getIntegratedLoyalties();
    }

    /**
     * Get external loyalties with allow redeem combobox.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing external loyalties with allow redeem combobox items.
     */
    @GetMapping(path = CUSTOMER_MEMBERSHIP_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_REDEEM_COMBOBOX, produces = "application/json")
    public GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean getExternalLoyaltiesWithAllowRedeemCombobox(
            @RequestParam(required = true) Integer customerId,
            @RequestParam(required = true) Integer branchId) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }
        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }

        return bookingService.getExternalLoyaltiesWithAllowRedeemCombobox(customerId, branchId);
    }
}

