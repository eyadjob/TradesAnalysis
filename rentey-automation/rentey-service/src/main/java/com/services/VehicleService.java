package com.services;

import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.vehicle.CreateVehiclesRequestBean;
import com.beans.vehicle.CreateVehiclesResponseBean;
import com.beans.vehicle.GetAllAccidentPoliciesResponseBean;
import com.beans.vehicle.GetAllCarModelsResponseBean;
import com.beans.vehicle.GetVendorComboboxItemsResponseBean;
import com.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Service for interacting with vehicle-related APIs.
 */
@Service
public class VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
    private static final Random random = new Random();

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @Autowired
    private LookupsService lookupsService;

    private static final Map<String, String> userDefinedVariables = PropertyManager.loadPropertyFileIntoMap("user-defined-variables.properties");
    /**
     * Get insurance company combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive insurance companies (default: false).
     * @param countryId The country ID for which to get the insurance companies (required).
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
     * @param countryId The country ID for which to get the accident policies (required).
     * @param includeInactive Whether to include inactive policies (default: false).
     * @param request The request query string containing pagination, filter, and sort parameters (optional).
     *                Example: "page=1&pageSize=15&filter=(isActive~eq~true~and~isExpired~eq~false)&sort=lastUpdateTime-desc"
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
     * Get all car models.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all car models.
     */
    public GetAllCarModelsResponseBean getAllCarModels() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/CarModel/GetAllCarModels")
                .retrieve()
                .bodyToMono(GetAllCarModelsResponseBean.class)
                .block();
    }

    /**
     * Get fuel types for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive fuel types (default: false).
     * @param countryId The country ID for which to get the fuel types (required).
     * @param selectedId The selected fuel type ID (default: -1).
     * @return The response containing all fuel types for combobox.
     */
    @Cacheable(cacheNames="fuelTypesForCombobox",value = "2Hours", keyGenerator = "AutoKeyGenerator")
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            Boolean includeInActive,
            Integer countryId,
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
     * @param branchId The branch ID for the vehicle (optional, will use first available if not provided).
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
                countryId, 
                false, 
                "page=1&pageSize=1&filter=(isActive~eq~true~and~isExpired~eq~false)&sort=lastUpdateTime-desc"
        );
        GetAllCarModelsResponseBean carModels = getAllCarModels();
        GetAllItemsComboboxItemsResponseBean fuelTypes = getFuelTypesForCombobox(false, countryId, -1);
        GetAllItemsComboboxItemsResponseBean fuelLevels = lookupsService.getAllItemsComboboxItems(12, false, false);
        GetAllItemsComboboxItemsResponseBean colors = lookupsService.getAllItemsComboboxItems(13, false, false);
        GetVendorComboboxItemsResponseBean vendors = getVendorComboboxItems(false);
        GetAllItemsComboboxItemsResponseBean usageTypes = lookupsService.getAllItemsComboboxItems(11, false, false);
        GetAllItemsComboboxItemsResponseBean licenseTypes = lookupsService.getAllItemsComboboxItems(10, false, false);
        // Lookup type 14 is called but not currently used in the payload structure
        GetAllItemsComboboxItemsResponseBean trimLevels = lookupsService.getAllItemsComboboxItems(14, false, false);

        // Extract values from responses (get first non-"Not Assigned" item, or first item if all are "Not Assigned")
        String insuranceCompanyId = getFirstValidValue(insuranceCompanies, "-1");
        Integer accidentPolicyId = accidentPolicies != null && 
                accidentPolicies.result() != null && 
                !accidentPolicies.result().data().isEmpty() 
                ? accidentPolicies.result().data().get(0).id() 
                : null;
        String modelId = carModels != null && 
                carModels.result() != null && 
                !carModels.result().items().isEmpty() 
                ? String.valueOf(carModels.result().items().get(0).id()) 
                : null;
        String fuelTypeId = lookupsService.getComboboxItemValueByDisplayText(fuelTypes, pRO);
        String licenseTypeId = getFirstValidValue(licenseTypes, "-1");
        String usageTypeId = getFirstValidValue(usageTypes, "-1");
        String vendorId = getFirstValidVendorValue(vendors, "-1");
        String colorId = getFirstValidValue(colors, "-1");
        String trimLevelId = getFirstValidValue(licenseTypes, "-1");

        // Generate random plate number (format: 3 letters + 4 digits)
        String randomPlateNumber = generateRandomPlateNumber();

        // Generate random chassis number (17 digits)
        String chassisNo = generateRandomChassisNumber();

        // Build the vehicle DTO
        CreateVehiclesRequestBean.VehicleDto vehicleDto = new CreateVehiclesRequestBean.VehicleDto(
                null, // isBulkUploaded
                "0", // odometer
                Integer.parseInt(fuelTypeId), // fuelId
                branchId != null ? branchId : "1012", // branchId (default if not provided)
                new CreateVehiclesRequestBean.VehicleManufacturingInfo(
                        modelId != null ? modelId : "1507", // modelId
                        2020 + random.nextInt(5), // year (2020-2024)
                        chassisNo // chassisNo
                ),
                new CreateVehiclesRequestBean.VehicleLicenseInfo(
                        licenseTypeId, // licenseTypeId
                        usageTypeId, // usageTypeId
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
                        vendorId, // vendorId
                        OffsetDateTime.now(), // date
                        String.valueOf(35000 + random.nextInt(50000)) // price (35000-84999)
                ),
                new CreateVehiclesRequestBean.VehicleSpecs(
                        colorId, // colorId
                        trimLevelId, // trimLevelId
                        fuelTypeId, // fuelTypeId
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
            plate.append(letter);
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
