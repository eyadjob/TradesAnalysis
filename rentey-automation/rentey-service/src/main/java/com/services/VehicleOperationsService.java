package com.services;

import com.annotation.LogExecutionTime;
import com.beans.vehicle.CreateVehiclesRequestBean;
import com.beans.vehicle.CreateVehiclesResponseBean;
import com.util.NumberUtil;
import com.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Service for vehicle operations that orchestrate multiple API calls.
 */
@Service
public class VehicleOperationsService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleOperationsService.class);
    private static final Random random = new Random();
    private static final Map<String, String> userDefinedVariables = PropertyManager.loadPropertyFileIntoMap("user-defined-variables.properties");
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private LookupsService lookupsService;
    
    @Autowired
    private CountryService countryService;

    /**
     * Create a vehicle with a random plate number by calling multiple APIs to gather required data.
     * This method calls various lookup APIs and uses their responses to build a complete vehicle creation request.
     *
     * @param countryName The country name for the vehicle (required).
     * @param branchName  The branch name for the vehicle (optional, will use first available if not provided).
     * @return The response containing the result of the vehicle creation operation.
     */
    @LogExecutionTime
    public CreateVehiclesResponseBean createVehicleWithRandomPlateNumber(String countryName, String branchName) {
        int countryId = Integer.parseInt(countryService.getOperationalCountryIdFromName(countryName));
        logger.info("Creating vehicle with random plate number for countryId: {}", countryId);
        
        // Call VehicleService methods through injected service to ensure caching works
        String insuranceCompanyId = vehicleService.getInsuranceCompanyIdByName(
                vehicleService.getInsuranceCompanyComboboxItems(countryId),
                userDefinedVariables.get("automationOrganizationName"));
        
        String accidentPolicyId = vehicleService.getAccidentPolicyNumberByOrganizationName(
                vehicleService.getAllAccidentPolicies(countryId),
                userDefinedVariables.get("automationOrganizationName"));
        
        String branchId = countryService.getBranchIdByName(
                countryService.getUserBranchesForCombobox(countryId, new ArrayList<>(List.of(8900, 8902))),
                branchName);
        
        String vehicleFuelTypeId = lookupsService.getComboboxItemValueByDisplayText(
                vehicleService.getFuelTypesForCombobox(countryId),
                userDefinedVariables.get("automationFuelTypeName"));
        
        String vehicleFuelLevelId = lookupsService.getComboboxItemValueByDisplayText(
                lookupsService.getAllItemsComboboxItems(12), "100%");
        
        String vehicleColorId = lookupsService.getComboboxItemValueByDisplayText(
                lookupsService.getAllItemsComboboxItems(13),
                userDefinedVariables.get("automationColourName"));
        
        String vehicleUsageTypeId = lookupsService.getComboboxItemValueByDisplayText(
                lookupsService.getAllItemsComboboxItems(11), "Rental");
        
        String vehicleLicenseTypeId = lookupsService.getComboboxItemValueByDisplayText(
                lookupsService.getAllItemsComboboxItems(10), "Private");
        
        String vehicleTrimLevelId = lookupsService.getComboboxItemValueByDisplayText(
                lookupsService.getAllItemsComboboxItems(14),
                userDefinedVariables.get("automationTrimLevel"));
        
        String vehicleVendorId = vehicleService.getVendorIdByName(
                vehicleService.getVendorComboboxItems(),
                userDefinedVariables.get("automationVendorName"));
        
        String carModelId = vehicleService.getCarModelIdByName(
                vehicleService.getAllCarModels(),
                userDefinedVariables.get("automationCarModelName"));
        
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
                        2000
                ),
                String.valueOf(countryId) // countryId
        );

        // Create request with single vehicle
        List<CreateVehiclesRequestBean.VehicleDto> vehicleDtos = new ArrayList<>();
        vehicleDtos.add(vehicleDto);
        CreateVehiclesRequestBean request = new CreateVehiclesRequestBean(vehicleDtos);

        logger.info("Created vehicle request with plate number: {}", randomPlateNumber);

        // Call createVehicles through VehicleService
        return vehicleService.createVehicles(request);
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

