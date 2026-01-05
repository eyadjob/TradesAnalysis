package com.controllers;

import com.beans.contract.ExecuteBookingRequestBean;
import com.beans.contract.ValidateCustomerResponseBean;
import com.beans.customer.IsCustomerEligibleForCustomerProvidersIntegrationResponseBean;
import com.beans.customer.SearchCustomerRequestBean;
import com.beans.general.AbpResponseBean;
import com.beans.vehicle.GetReadyVehiclesByCategoryAndModelRequestBean;
import com.beans.vehicle.GetReadyVehiclesByCategoryAndModelResponseBean;
import com.beans.vehicle.GetReadyVehiclesModelResponseBean;
import com.services.ExecuteBookingOperations;
import com.services.ExecuteBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * REST controller for execute booking-related operations.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class ExecuteBookingController {

    @Autowired
    private ExecuteBookingService executeBookingService;

    @Autowired
    private ExecuteBookingOperations executeBookingOperations;

    /**
     * Search customer by customer ID, phone number, or identity number.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing customer ID, phone number, or identity number.
     * @return The response containing matching customer information.
     */
    @PostMapping(path = CUSTOMER_SEARCH_CUSTOMER, consumes = "application/json", produces = "application/json")
    public AbpResponseBean searchCustomer(
            @RequestBody(required = true) SearchCustomerRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return executeBookingService.searchCustomer(request);
    }

    /**
     * Check if booking is allowed to be executed.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param id The booking ID (required, e.g., 66731).
     * @return The response containing the result indicating if the booking is allowed to be executed.
     */
    @PostMapping(path = BOOKING_IS_ALLOWED_TO_EXECUTE_BOOKING, produces = "application/json")
    public AbpResponseBean isAllowedToExecuteBooking(
            @RequestParam(required = true) Long id) {

        if (id == null) {
            throw new IllegalArgumentException("id parameter is required.");
        }

        return executeBookingService.isAllowedToExecuteBooking(id);
    }

    /**
     * Get lite customer by customer ID.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param id The customer ID (required, e.g., 303206).
     * @return The response containing lite customer information.
     */
    @GetMapping(path = CUSTOMER_GET_LITE_CUSTOMER, produces = "application/json")
    public AbpResponseBean getLiteCustomer(
            @RequestParam(required = true) Long id) {

        if (id == null) {
            throw new IllegalArgumentException("id parameter is required.");
        }

        return executeBookingService.getLiteCustomer(id);
    }

    /**
     * Get lite car model by model ID.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param modelId The car model ID (required, e.g., 291).
     * @return The response containing lite car model information.
     */
    @GetMapping(path = CAR_MODEL_GET_LITE_CAR_MODEL, produces = "application/json")
    public AbpResponseBean getLiteCarModel(
            @RequestParam(required = true) Integer modelId) {

        if (modelId == null) {
            throw new IllegalArgumentException("modelId parameter is required.");
        }

        return executeBookingService.getLiteCarModel(modelId);
    }

    /**
     * Get ready vehicles model by branch ID, category ID, and mode ID.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param branchId The branch ID (required).
     * @param categoryId The category ID (required).
     * @param modeId The mode ID (required).
     * @return The response containing ready vehicles model information.
     */
    @GetMapping(path = RENTAL_VEHICLE_GET_READY_VEHICLES_MODEL, produces = "application/json")
    public GetReadyVehiclesModelResponseBean getReadyVehiclesModel(
            @RequestParam(required = true) Integer branchId,
            @RequestParam(required = true) Integer categoryId,
            @RequestParam(required = true) Integer modeId) {

        if (branchId == null) {
            throw new IllegalArgumentException("branchId parameter is required.");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId parameter is required.");
        }
        if (modeId == null) {
            throw new IllegalArgumentException("modeId parameter is required.");
        }

        return executeBookingService.getReadyVehiclesModel(branchId, categoryId, modeId);
    }

    /**
     * Get ready vehicles by category and model.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing category ID, model ID, branch ID, isBooking, and isMonthlyContract.
     * @return The response containing ready vehicles information.
     */
    @PostMapping(path = RENTAL_VEHICLE_GET_READY_VEHICLES_BY_CATEGORY_AND_MODEL, consumes = "application/json", produces = "application/json")
    public GetReadyVehiclesByCategoryAndModelResponseBean getReadyVehiclesByCategoryAndModel(
            @RequestBody(required = true) GetReadyVehiclesByCategoryAndModelRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return executeBookingService.getReadyVehiclesByCategoryAndModel(request);
    }

    /**
     * Check if customer is eligible for customer providers integration.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param customerId The customer ID (required, e.g., 303206).
     * @return The response containing customer eligibility information.
     */
    @PostMapping(path = CUSTOMER_IS_CUSTOMER_ELIGIBLE_FOR_CUSTOMER_PROVIDERS_INTEGRATION, produces = "application/json")
    public IsCustomerEligibleForCustomerProvidersIntegrationResponseBean isCustomerEligibleForCustomerProvidersIntegration(
            @RequestParam(required = true) Long customerId) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }

        return executeBookingService.isCustomerEligibleForCustomerProvidersIntegration(customerId);
    }

    /**
     * Validate customer for contract.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param customerId The customer ID (required, e.g., 303206).
     * @return The response containing customer validation information.
     */
    @PostMapping(path = CONTRACT_VALIDATE_CUSTOMER, produces = "application/json")
    public ValidateCustomerResponseBean validateCustomer(
            @RequestParam(required = true) Long customerId) {

        if (customerId == null) {
            throw new IllegalArgumentException("customerId parameter is required.");
        }

        return executeBookingService.validateCustomer(customerId);
    }

    /**
     * Execute booking.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing booking execution information.
     * @return The response containing the execution result.
     */
    @PostMapping(path = CONTRACT_EXECUTE_BOOKING, consumes = "application/json", produces = "application/json")
    public AbpResponseBean executeBooking(
            @RequestBody(required = true) ExecuteBookingRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        return executeBookingService.executeBooking(request);
    }

    /**
     * Execute created booking with new customer and new vehicle.
     * This endpoint orchestrates a series of API calls to execute a booking that was previously created.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryName The country name (required).
     * @param branchName The branch name (required).
     * @return The response containing the execution result.
     */
    @PostMapping(path = BOOKING_EXECUTE_CREATED_BOOKING_WITH_NEW_CUSTOMER_AND_NEW_VEHICLE, produces = "application/json")
    public AbpResponseBean executeCreatedBookingWithNewCustomerAndNewVehicle(
            @RequestParam(required = true) String countryName,
            @RequestParam(required = true) String branchName) {

        if (countryName == null || countryName.trim().isEmpty()) {
            throw new IllegalArgumentException("countryName parameter is required and cannot be empty.");
        }
        if (branchName == null || branchName.trim().isEmpty()) {
            throw new IllegalArgumentException("branchName parameter is required and cannot be empty.");
        }

        return executeBookingOperations.ExecuteCreatedBookingWithNewCustomerAndNewVehicle(countryName, branchName);
    }
}

