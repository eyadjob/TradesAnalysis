package com.services;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.vehicle.*;
import com.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Service for interacting with vehicle-related APIs.
 */
@Service
public class VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
    private static final Random random = new Random();
    private static final Map<String, String> userDefinedVariables = PropertyManager.loadPropertyFileIntoMap("user-defined-variables.properties");
    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;
    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;
    @Autowired
    private LookupsService lookupsService;

    /**
     * Get insurance company combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive insurance companies (default: false).
     * @param countryId       The country ID for which to get the insurance companies (required).
     * @return The response containing all insurance company combobox items.
     */
    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(
            Boolean includeInActive,
            Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/InsuranceCompany/GetInsuranceCompanyComboboxItems");

                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Get all accident policies.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the accident policies (required).
     * @param includeInactive Whether to include inactive policies (default: false).
     * @param request         The request query string containing pagination, filter, and sort parameters (optional).
     *                        Example: "page=1&pageSize=15&filter=(isActive~eq~true~and~isExpired~eq~false)&sort=lastUpdateTime-desc"
     * @return The response containing all accident policies.
     */
    public GetAllAccidentPoliciesResponseBean getAllAccidentPolicies(
            Integer countryId,
            Boolean includeInactive,
            String request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/AccidentPolicy/GetAllAccidentPolicies");

                    if (countryId != null) {
                        builder.queryParam("CountryId", countryId);
                    }
                    if (includeInactive != null) {
                        builder.queryParam("IncludeInactive", includeInactive);
                    }
                    if (request != null && !request.isEmpty()) {
                        builder.queryParam("Request", request);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllAccidentPoliciesResponseBean.class)
                .block();
    }

    /**
     * Get all accident policies.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the accident policies (required).
     * @return The response containing all accident policies.
     */
    public GetAllAccidentPoliciesResponseBean getAllAccidentPolicies(
            Integer countryId
            ) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/AccidentPolicy/GetAllAccidentPolicies");
                        builder.queryParam("CountryId", countryId);
                        builder.queryParam("IncludeInactive", false);
                        builder.queryParam("Request", "page=1&pageSize=15&filter=(isActive~eq~true~and~isExpired~eq~false)&sort=lastUpdateTime-desc");

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllAccidentPoliciesResponseBean.class)
                .block();
    }

    /**
     * Get all car models.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all car models.
     */
    @Cacheable(cacheNames = "allCarsModelsCache", value = "2Hours", keyGenerator = "AutoKeyGenerator")
    public GetAllCarModelsResponseBean getAllCarModels() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/CarModel/GetAllCarModels")
                .retrieve()
                .bodyToMono(GetAllCarModelsResponseBean.class)
                .block();
    }

    /**
     * Get car model ID by name.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param carModelsResponseBean The response containing all car models.
     * @param carModelName The name of the car model to find.
     * @return The ID of the car model, or "-1" if not found.
     */
    public String getCarModelIdByName(GetAllCarModelsResponseBean carModelsResponseBean, String carModelName) {
        return carModelsResponseBean.result().items().stream()
                .filter(carModel -> carModel.name() != null && carModel.name().equals(carModelName))
                .findFirst()
                .map(carModel -> String.valueOf(carModel.id()))
                .orElse("-1");
    }

    /**
     * Get fuel types for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the fuel types (required).
     * @param includeInActive Whether to include inactive fuel types (default: false).
     * @param selectedId      The selected fuel type ID (default: -1).
     * @return The response containing all fuel types for combobox.
     */
    @Cacheable(cacheNames = "fuelTypesForCombobox", value = "2Hours", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            Integer countryId, Boolean includeInActive,
            Integer selectedId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/FuelType/GetFuelTypesForCombobox");

                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }
                    if (selectedId != null) {
                        builder.queryParam("selectedId", selectedId);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Get fuel types for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the fuel types (required).
     *                  * @return The response containing all fuel types for combobox.
     */
    @Cacheable(cacheNames = "fuelTypesForCombobox", value = "2Hours", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            Integer countryId
    ) {
       return getFuelTypesForCombobox(countryId,false , -1);
    }

    /**
     * Get vendor combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInactive Whether to include inactive vendors (default: false).
     * @return The response containing all vendor combobox items.
     */
    public GetVendorComboboxItemsResponseBean getVendorComboboxItems(Boolean includeInactive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Vendor/GetVendorComboboxItems");

                    if (includeInactive != null) {
                        builder.queryParam("IncludeInactive", includeInactive);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetVendorComboboxItemsResponseBean.class)
                .block();
    }


    /**
     * Get vendor combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * @return The response containing all vendor combobox items.
     */
    public GetVendorComboboxItemsResponseBean getVendorComboboxItems() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return getVendorComboboxItems(false);
    }

    public String getVendorIdByName(GetVendorComboboxItemsResponseBean vendorComboboxItemsResponseBean, String vendorName) {
        return vendorComboboxItemsResponseBean.result().items().stream()
                .filter(vendorComboboxItem -> vendorComboboxItem.displayText().equals(vendorName))
                .findFirst()
                .map(vehicleVendor -> String.valueOf(vehicleVendor.value()))
                .orElse("-1");
    }

    /**
     * Create vehicles.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing vehicle information to create.
     * @return The response containing the result of the operation.
     */
    public CreateVehiclesResponseBean createVehicles(CreateVehiclesRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Vehicle/CreateVehicles")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateVehiclesResponseBean.class)
                .block();
    }

    /**
     * Create a vehicle with a random plate number by calling multiple APIs to gather required data.
     * This method calls various lookup APIs and uses their responses to build a complete vehicle creation request.
     *
     * @param countryId The country ID for the vehicle (required, default: 1).
     * @param branchId  The branch ID for the vehicle (optional, will use first available if not provided).
     * @return The response containing the result of the vehicle creation operation.
     */
    public CreateVehiclesResponseBean createVehicleWithRandomPlateNumber(Integer countryId, String branchId) {
        if (countryId == null) {
            countryId = 1; // Default to country 1
        }
        logger.info("Creating vehicle with random plate number for countryId: {}", countryId);

        // Call all required methods to gather data
        GetAllItemsComboboxItemsResponseBean insuranceCompanies = getInsuranceCompanyComboboxItems(false, countryId);
        GetAllAccidentPoliciesResponseBean accidentPolicies = getAllAccidentPolicies(
                countryId
        );

        String  vehicleFuelTypeId = lookupsService.getComboboxItemValueByDisplayText(getFuelTypesForCombobox(countryId), userDefinedVariables.get("automationFuelTypeName"));
        String vehicleFuelLevelId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(12),"100%");
        String vehicleColorId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(13),userDefinedVariables.get("automationColourName"));
        String vehicleUsageTypeId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(11),"Rental");
        String vehicleLicenseTypeId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(10),"Private");
        String vehicleTrimLevelId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(14),userDefinedVariables.get("automationTrimLevel"));
        String vehicleVendorId = getVendorIdByName(getVendorComboboxItems(),userDefinedVariables.get("automationVendorName"));
        String carModelId = getCarModelIdByName(getAllCarModels(),userDefinedVariables.get("automationCarModelName"));

        // Extract values from responses (get first non-"Not Assigned" item, or first item if all are "Not Assigned")
        String insuranceCompanyId = getFirstValidValue(insuranceCompanies, "-1");
        Integer accidentPolicyId = accidentPolicies != null &&
                accidentPolicies.result() != null &&
                !accidentPolicies.result().data().isEmpty()
                ? accidentPolicies.result().data().get(0).id()
                : null;

        String randomPlateNumber = generateRandomPlateNumber();
        // Generate random chassis number (17 digits)
        String chassisNo = generateRandomChassisNumber();

        // Build the vehicle DTO
        CreateVehiclesRequestBean.VehicleDto vehicleDto = new CreateVehiclesRequestBean.VehicleDto(
                null, // isBulkUploaded
                "0", // odometer
                Integer.parseInt(vehicleFuelLevelId), // fuelLevelId
                branchId != null ? branchId : "1012", // branchId (default if not provided)
                new CreateVehiclesRequestBean.VehicleManufacturingInfo(
                        carModelId, // modelId
                        2020 + random.nextInt(5), // year (2020-2024)
                        chassisNo // chassisNo
                ),
                new CreateVehiclesRequestBean.VehicleLicenseInfo(
                        vehicleLicenseTypeId, // licenseTypeId
                        vehicleUsageTypeId, // usageTypeId
                        randomPlateNumber // plateNo
                ),
                new CreateVehiclesRequestBean.VehicleInsuranceInfo(
                        OffsetDateTime.now().plusYears(1).toString().replace("T", " ").substring(0, 19), // expiryDate (1 year from now)
                        "2180878653", // number
                        insuranceCompanyId, // insuranceCompanyId
                        accidentPolicyId // accidentPolicyId
                ),
                new CreateVehiclesRequestBean.LocationInfo(
                        branchId != null ? branchId : "1012" // currentLocationId
                ),
                new CreateVehiclesRequestBean.PurchaseInfo(
                        vehicleVendorId, // vendorId
                        OffsetDateTime.now(), // date
                        String.valueOf(35000 + random.nextInt(50000)) // price (35000-84999)
                ),
                new CreateVehiclesRequestBean.VehicleSpecs(
                        vehicleColorId, // colorId
                        vehicleTrimLevelId, // trimLevelId
                        vehicleFuelTypeId, // fuelTypeId
                        50 + random.nextInt(30), // fuelTankSize (50-79)
                        1000 + random.nextInt(2000) // engineSize (1000-2999)
                ),
                String.valueOf(countryId) // countryId
        );

        // Create request with single vehicle
        List<CreateVehiclesRequestBean.VehicleDto> vehicleDtos = new ArrayList<>();
        vehicleDtos.add(vehicleDto);
        CreateVehiclesRequestBean request = new CreateVehiclesRequestBean(vehicleDtos);

        logger.info("Created vehicle request with plate number: {}", randomPlateNumber);

        // Call createVehicles
        return createVehicles(request);
    }

    /**
     * Helper method to get first valid value from combobox items (excluding "Not Assigned").
     */
    private String getFirstValidValue(GetAllItemsComboboxItemsResponseBean response, String defaultValue) {
        if (response == null || response.result() == null || response.result().items() == null || response.result().items().isEmpty()) {
            return defaultValue;
        }

        for (GetAllItemsComboboxItemsResponseBean.ComboboxItem item : response.result().items()) {
            if (item.value() != null && !item.value().equals("-1") && !"Not Assigned".equals(item.displayText())) {
                return item.value();
            }
        }

        // If all are "Not Assigned", return first value
        return response.result().items().get(0).value();
    }

    /**
     * Helper method to get first valid vendor value (excluding "Not Assigned").
     */
    private String getFirstValidVendorValue(GetVendorComboboxItemsResponseBean response, String defaultValue) {
        if (response == null || response.result() == null || response.result().items() == null || response.result().items().isEmpty()) {
            return defaultValue;
        }

        for (GetVendorComboboxItemsResponseBean.VendorComboboxItem item : response.result().items()) {
            if (item.value() != null && !item.value().equals("-1") && !"Not Assigned".equals(item.displayText())) {
                return item.value();
            }
        }

        // If all are "Not Assigned", return first value
        return response.result().items().get(0).value();
    }

    /**
     * Generate a random plate number (format: 3 letters + space + 4 digits).
     */
    private String generateRandomPlateNumber() {
        StringBuilder plate = new StringBuilder();

        // Generate 3 random letters
        for (int i = 0; i < 3; i++) {
            char letter = (char) ('A' + random.nextInt(26));
            plate.append(letter).append(" ");
        }

        plate.append(" ");

        // Generate 4 random digits
        for (int i = 0; i < 4; i++) {
            plate.append(random.nextInt(10));
        }

        return plate.toString();
    }

    /**
     * Generate a random 17-digit chassis number.
     */
    private String generateRandomChassisNumber() {
        StringBuilder chassis = new StringBuilder();
        for (int i = 0; i < 17; i++) {
            chassis.append(random.nextInt(10));
        }
        return chassis.toString();
    }
}
