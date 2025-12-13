package com.controllers;

import com.beans.general.AbpResponseBean;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.general.UploadBase64FileRequestBean;
import com.beans.general.UploadBase64FileResponseBean;
import com.beans.vehicle.CreateVehiclesRequestBean;
import com.beans.vehicle.CreateVehiclesResponseBean;
import com.beans.vehicle.GetAllAccidentPoliciesResponseBean;
import com.beans.vehicle.GetAllBranchVehiclesResponseBean;
import com.beans.vehicle.GetAllCarModelsResponseBean;
import com.beans.vehicle.GetVendorComboboxItemsResponseBean;
import com.beans.vehicle.GetVehicleCheckPreparationDataResponseBean;
import com.beans.vehicle.ReceiveNewVehicleRequestBean;
import com.services.VehicleOperationsService;
import com.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.controllers.ApiPaths.*;

/**
 * REST controller for vehicle-related operations.
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private VehicleOperationsService vehicleOperationsService;

    /**
     * Get insurance company combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive insurance companies (default: false).
     * @param countryId The country ID for which to get the insurance companies (required).
     * @return The response containing all insurance company combobox items.
     */
    @GetMapping(path = INSURANCE_COMPANY_GET_COMBOBOX_ITEMS, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = true) Integer countryId) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return vehicleService.getInsuranceCompanyComboboxItems(countryId, includeInActive);
    }

    /**
     * Get all accident policies.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The country ID for which to get the accident policies (required).
     * @param includeInactive Whether to include inactive policies (default: false).
     * @param request The request query string containing pagination, filter, and sort parameters (optional).
     *                Example: "page=1&pageSize=15&filter=(isActive~eq~true~and~isExpired~eq~false)&sort=lastUpdateTime-desc"
     * @return The response containing all accident policies.
     */
    @GetMapping(path = ACCIDENT_POLICY_GET_ALL, produces = "application/json")
    public GetAllAccidentPoliciesResponseBean getAllAccidentPolicies(
            @RequestParam(required = true) Integer countryId,
            @RequestParam(required = false, defaultValue = "false") Boolean includeInactive,
            @RequestParam(required = false) String request) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return vehicleService.getAllAccidentPolicies(countryId, includeInactive, request);
    }

    /**
     * Get all car models.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @return The response containing all car models.
     */
    @GetMapping(path = CAR_MODEL_GET_ALL, produces = "application/json")
    public GetAllCarModelsResponseBean getAllCarModels() {
        return vehicleService.getAllCarModels();
    }

    /**
     * Get fuel types for combobox.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInActive Whether to include inactive fuel types (default: false).
     * @param countryId The country ID for which to get the fuel types (required).
     * @param selectedId The selected fuel type ID (default: -1).
     * @return The response containing all fuel types for combobox.
     */
    @GetMapping(path = FUEL_TYPE_GET_FOR_COMBOBOX, produces = "application/json")
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInActive,
            @RequestParam(required = true) Integer countryId,
            @RequestParam(required = false, defaultValue = "-1") Integer selectedId) {

        if (countryId == null) {
            throw new IllegalArgumentException("countryId parameter is required.");
        }

        return vehicleService.getFuelTypesForCombobox(countryId, includeInActive, selectedId);
    }

    /**
     * Get vendor combobox items.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param includeInactive Whether to include inactive vendors (default: false).
     * @return The response containing all vendor combobox items.
     */
    @GetMapping(path = VENDOR_GET_COMBOBOX_ITEMS, produces = "application/json")
    public GetVendorComboboxItemsResponseBean getVendorComboboxItems(
            @RequestParam(required = false, defaultValue = "false") Boolean includeInactive) {

        return vehicleService.getVendorComboboxItems(includeInactive);
    }

    /**
     * Create vehicles.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing vehicle information to create.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = VEHICLE_CREATE, consumes = "application/json", produces = "application/json")
    public CreateVehiclesResponseBean createVehicles(
            @RequestBody(required = true) CreateVehiclesRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (request.vehicleDtos() == null || request.vehicleDtos().isEmpty()) {
            throw new IllegalArgumentException("vehicleDtos is required and cannot be empty.");
        }

        return vehicleService.createVehicles(request);
    }

    /**
     * Get all branch vehicles.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param countryId The ID of the country to filter vehicles (required).
     * @param branchId The ID of the branch to get vehicles from (required).
     * @param plateNumber The plate number or part of it to search for (optional).
     * @return The response containing all branch vehicles matching the criteria.
     */
    @GetMapping(path = RENTAL_VEHICLE_GET_ALL_BRANCH_VEHICLES, produces = "application/json")
    public GetAllBranchVehiclesResponseBean getAllBranchVehicles(
            @RequestParam(required = true) int countryId,
            @RequestParam(required = true) int branchId,
            @RequestParam(required = false) String plateNumber) {
        return vehicleService.getAllBranchVehicles(countryId, branchId, 
            plateNumber != null ? plateNumber : "");
    }

    /**
     * Get vehicle check preparation data.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param vehicleId The vehicle ID (required).
     * @param checkTypeId The check type ID (required).
     * @param sourceId The source ID (required).
     * @return The response containing vehicle check preparation data.
     */
    @GetMapping(path = VEHICLE_CHECK_GET_PREPARATION_DATA, produces = "application/json")
    public GetVehicleCheckPreparationDataResponseBean getVehicleCheckPreparationData(
            @RequestParam(required = true) Integer vehicleId,
            @RequestParam(required = true) Integer checkTypeId,
            @RequestParam(required = true) Integer sourceId) {

        if (vehicleId == null) {
            throw new IllegalArgumentException("vehicleId parameter is required.");
        }
        if (checkTypeId == null) {
            throw new IllegalArgumentException("checkTypeId parameter is required.");
        }
        if (sourceId == null) {
            throw new IllegalArgumentException("sourceId parameter is required.");
        }

        return vehicleService.getVehicleCheckPreparationData(vehicleId, checkTypeId, sourceId);
    }

    /**
     * Upload base64 file.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing base64 encoded file data (e.g., "data:image/jpeg;base64,...").
     * @return The response containing the uploaded file information.
     */
    @PostMapping(path = FILE_UPLOAD_BASE64, consumes = "application/json", produces = "application/json")
    public UploadBase64FileResponseBean uploadBase64File(
            @RequestBody(required = true) UploadBase64FileRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (request.data() == null || request.data().isEmpty()) {
            throw new IllegalArgumentException("data field is required and cannot be empty.");
        }

        return vehicleService.uploadBase64File(request);
    }

    /**
     * Receive new vehicle.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     *
     * @param request The request containing vehicle check information for receiving a new vehicle.
     * @return The response containing the result of the operation.
     */
    @PostMapping(path = RENTAL_VEHICLE_RECEIVE_NEW_VEHICLE, consumes = "application/json", produces = "application/json")
    public AbpResponseBean receiveNewVehicle(
            @RequestBody(required = true) ReceiveNewVehicleRequestBean request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required and cannot be null.");
        }

        if (request.vehicleId() == null) {
            throw new IllegalArgumentException("vehicleId is required and cannot be null.");
        }

        return vehicleService.receiveNewVehicle(request);
    }

    /**
     * Create a vehicle with a random plate number.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * This method orchestrates multiple API calls to gather required data and creates a vehicle
     * with randomly generated plate number and chassis number.
     *
     * @param countryName The country ID for the vehicle (optional, default: 1).
     * @param branchName The branch name for the vehicle (optional, will use first available if not provided).
     * @return The response containing the result of the vehicle creation operation.
     */
    @GetMapping(path = VEHICLE_CREATE_WITH_RANDOM_PLATE, produces = "application/json")
    public CreateVehiclesResponseBean createVehicleWithRandomPlateNumber(
            @RequestParam(required = false) String countryName,
            @RequestParam(required = false) String branchName) {

        return vehicleOperationsService.createVehicleWithRandomPlateNumber(countryName, branchName);
    }

    /**
     * Create a vehicle with random plate number and then receive it.
     * This endpoint automatically calls the authorization-service to get the refreshToken
     * and uses it in the Authorization header when calling the external API.
     * This method orchestrates the creation of a vehicle and then immediately receives it
     * by calling the ReceiveNewVehicle API with proper payload.
     *
     * @param countryName The country name for the vehicle (required).
     * @param branchName The branch name for the vehicle (optional, will use first available if not provided).
     * @return The response containing the result of the receive new vehicle operation.
     */
    @GetMapping(path = VEHICLE_CREATE_AND_RECEIVE, produces = "application/json")
    public AbpResponseBean createAndReceiveNewVehicle(
            @RequestParam(required = true) String countryName,
            @RequestParam(required = false) String branchName) {

        if (countryName == null || countryName.isEmpty()) {
            throw new IllegalArgumentException("countryName parameter is required and cannot be empty.");
        }

        return vehicleOperationsService.createAndReceiveNewVehicle(countryName, branchName);
    }
}
