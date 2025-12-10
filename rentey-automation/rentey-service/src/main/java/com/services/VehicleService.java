package com.services;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.vehicle.*;
import com.util.NumberUtil;
import com.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    @Autowired
    private CountryService countryService;

    /**
     * Get insurance company combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the insurance companies (required).
     * @param includeInActive Whether to include inactive insurance companies (default: false).
     * @return The response containing all insurance company combobox items.
     */
    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(
            Integer countryId, Boolean includeInActive) {
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

    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(int countryId) {
        return getInsuranceCompanyComboboxItems(countryId,false);
    }

    public String getInsuranceCompanyIdByName(GetAllItemsComboboxItemsResponseBean insuranceResponseBean, String insuranceCompanyName) {
        return insuranceResponseBean.result().items().stream().filter(insuranceCompany -> insuranceCompany.displayText().equals(insuranceCompanyName))
                .findFirst().map(insuranceCompany -> String.valueOf(insuranceCompany.value())).orElse("-1");
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
     * @param countryId The country ID for which to get the accident policies (required).
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

    public String getAccidentPolicyNumberByOrganizationName(GetAllAccidentPoliciesResponseBean accidentPoliciesResponseBean, String accidentPolicyInsuranceName) {
        return accidentPoliciesResponseBean.result().data().stream().filter(accidentPolicy -> accidentPolicy.insuranceCompany().equals(accidentPolicyInsuranceName))
                .findFirst().map(accidentPolicy -> String.valueOf(accidentPolicy.id())).orElse("-1");
    }

    /**
     * Get all car models.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all car models.
     */
    @Cacheable(cacheNames = "allCarsModelsCache", keyGenerator = "AutoKeyGenerator")
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
     * @param carModelName          The name of the car model to find.
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
    @Cacheable(cacheNames = "fuelTypesForCombobox", keyGenerator = "AutoKeyGenerator")
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
    @Cacheable(cacheNames = "fuelTypesForCombobox", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            Integer countryId
    ) {
        return getFuelTypesForCombobox(countryId, false, -1);
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
     *
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
     * @param countryName The country ID for the vehicle (required, default: 1).
     * @param branchName  The branch ID for the vehicle (optional, will use first available if not provided).
     * @return The response containing the result of the vehicle creation operation.
     */
    public CreateVehiclesResponseBean createVehicleWithRandomPlateNumber(String countryName, String branchName) {
        int countryId = Integer.parseInt(countryService.getOperationalCountryIdFromName(countryName));
        logger.info("Creating vehicle with random plate number for countryId: {}", countryId);
        String insuranceCompanyId = getInsuranceCompanyIdByName(getInsuranceCompanyComboboxItems(countryId),userDefinedVariables.get("automationOrganizationName"));
        String accidentPolicyId = getAccidentPolicyNumberByOrganizationName(getAllAccidentPolicies(countryId),userDefinedVariables.get("automationOrganizationName"));
        String branchId = countryService.getBranchIdByName(countryService.getUserBranchesForCombobox(countryId,new ArrayList<>(List.of(8900, 8902))),branchName);
        String vehicleFuelTypeId = lookupsService.getComboboxItemValueByDisplayText(getFuelTypesForCombobox(countryId), userDefinedVariables.get("automationFuelTypeName"));
        String vehicleFuelLevelId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(12), "100%");
        String vehicleColorId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(13), userDefinedVariables.get("automationColourName"));
        String vehicleUsageTypeId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(11), "Rental");
        String vehicleLicenseTypeId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(10), "Private");
        String vehicleTrimLevelId = lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(14), userDefinedVariables.get("automationTrimLevel"));
        String vehicleVendorId = getVendorIdByName(getVendorComboboxItems(), userDefinedVariables.get("automationVendorName"));
        String carModelId = getCarModelIdByName(getAllCarModels(), userDefinedVariables.get("automationCarModelName"));
        String randomPlateNumber = generateRandomPlateNumber();
        String chassisNo = NumberUtil.generateRandomNumericString(17);

        // Build the vehicle DTO
        CreateVehiclesRequestBean.VehicleDto vehicleDto = new CreateVehiclesRequestBean.VehicleDto(
                null, // isBulkUploaded
                "22", // odometer
                Integer.parseInt(vehicleFuelLevelId), // fuelLevelId
                branchId, // branchId (default if not provided)
                new CreateVehiclesRequestBean.VehicleManufacturingInfo(
                        carModelId, // modelId
                        2020, // year (2020-2024)
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
                        Integer.parseInt(accidentPolicyId) // accidentPolicyId
                ),
                new CreateVehiclesRequestBean.LocationInfo(
                        branchId != null ? branchId : "1012" // currentLocationId
                ),
                new CreateVehiclesRequestBean.PurchaseInfo(
                        vehicleVendorId, // vendorId
                        OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC), // date formatted as "2025-12-07T12:41:40"
                        String.valueOf(35000 + random.nextInt(50000))
                ),
                new CreateVehiclesRequestBean.VehicleSpecs(
                        vehicleColorId, // colorId
                        vehicleTrimLevelId, // trimLevelId
                        vehicleFuelTypeId, // fuelTypeId
                        50 + random.nextInt(30),
                        1000 + random.nextInt(2000)
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

        return plate.toString().replace("  ", " ");
    }

}
