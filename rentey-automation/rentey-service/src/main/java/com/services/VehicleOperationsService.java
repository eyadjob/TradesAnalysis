package com.services;

import com.annotation.LogExecutionTime;
import com.beans.general.AbpResponseBean;
import com.beans.vehicle.*;
import com.util.NumberUtil;
import com.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
     * Create a vehicle with random plate number and then receive it.
     * This method orchestrates the creation of a vehicle and then immediately receives it
     * by calling the ReceiveNewVehicle API with proper payload.
     *
     * @param countryName The country name for the vehicle (required).
     * @param branchName  The branch name for the vehicle (optional, will use first available if not provided).
     * @return The response containing the result of the receive new vehicle operation.
     */
    @LogExecutionTime
    public AbpResponseBean createAndReceiveNewVehicle(String countryName, String branchName) {
        logger.info("Creating and receiving new vehicle for country: {}, branch: {}", countryName, branchName);
        
        // Step 1: Create the vehicle with random plate number
        CreateVehiclesResponseBean createResponse = createVehicleWithRandomPlateNumber(countryName, branchName);
        
        if (createResponse == null || !Boolean.TRUE.equals(createResponse.success())) {
            logger.error("Failed to create vehicle. Response: {}", createResponse);
            throw new RuntimeException("Failed to create vehicle: " + (createResponse != null ? createResponse.error() : "Unknown error"));
        }
        
        // Step 2: Get the plate number from the created vehicle
        // We need to extract the plate number from the request we just made
        // Since createVehicleWithRandomPlateNumber generates it internally, we'll query for it
        int countryId = Integer.parseInt(countryService.getOperationalCountryIdFromName(countryName));
        String branchId = countryService.getBranchIdByName(
                countryService.getUserBranchesForCombobox(countryId, new ArrayList<>(List.of(8900, 8902))),
                branchName);
        
        // Step 3: Find the newly created vehicle by querying all branch vehicles
        // We'll use a filter to find vehicles created recently
        String filter = String.format("(countryId~eq~%d~and~currentLocationId~eq~%s)", countryId, branchId);
        String encodedFilter = URLEncoder.encode(filter, StandardCharsets.UTF_8);
        String request = String.format("page=1&pageSize=1&sort=lastModificationTime-desc&filter=%s", encodedFilter);
        
        GetAllBranchVehiclesResponseBean vehiclesResponse = vehicleService.getAllBranchVehicles(request);
        
        if (vehiclesResponse == null || vehiclesResponse.result() == null || 
            vehiclesResponse.result().data() == null || vehiclesResponse.result().data().isEmpty()) {
            logger.error("Failed to find created vehicle");
            throw new RuntimeException("Failed to find created vehicle");
        }
        
        GetAllBranchVehiclesResponseBean.BranchVehicle createdVehicle = vehiclesResponse.result().data().get(0);
        Integer vehicleId = createdVehicle.id();
        String plateNumber = createdVehicle.plateNo();
        
        logger.info("Found created vehicle with ID: {}, plate number: {}", vehicleId, plateNumber);
        
        // Step 4: Get vehicle check preparation data
        Integer checkTypeId = 6; // Receive New Vehicle
        Integer sourceId = 120;
        GetVehicleCheckPreparationDataResponseBean preparationData = 
                vehicleService.getVehicleCheckPreparationData(vehicleId, checkTypeId, sourceId);
        
        if (preparationData == null || preparationData.result() == null) {
            logger.error("Failed to get vehicle check preparation data");
            throw new RuntimeException("Failed to get vehicle check preparation data");
        }
        
        GetVehicleCheckPreparationDataResponseBean.VehicleCheckPreparationDataResult prepResult = 
                preparationData.result();
        
        // Step 5: Build the ReceiveNewVehicle request payload
        ReceiveNewVehicleRequestBean receiveRequest = buildReceiveNewVehicleRequest(
                vehicleId,
                prepResult,
                createdVehicle
        );
        
        // Step 6: Call receiveNewVehicle
        logger.info("Receiving new vehicle with ID: {}", vehicleId);
        return vehicleService.receiveNewVehicle(receiveRequest);
    }
    
    /**
     * Build the ReceiveNewVehicle request payload from preparation data.
     */
    private ReceiveNewVehicleRequestBean buildReceiveNewVehicleRequest(
            Integer vehicleId,
            GetVehicleCheckPreparationDataResponseBean.VehicleCheckPreparationDataResult prepResult,
            GetAllBranchVehiclesResponseBean.BranchVehicle vehicle) {
        
        // Get fuel ID from preparation data or vehicle
        Integer fuelId = prepResult.fuelId() != null ? prepResult.fuelId() : vehicle.fuelId();
        
        // Get odometer from preparation data or vehicle (use minimum if available)
        Integer odometer = prepResult.odometer() != null ? prepResult.odometer() : 
                          (prepResult.minimumOdomter() != null ? prepResult.minimumOdomter() : 
                           (vehicle.odometer() != null ? vehicle.odometer() : 22));
        
        // Build signature (using a placeholder URL - in real scenario, this would be uploaded)
        ReceiveNewVehicleRequestBean.Signature signature = new ReceiveNewVehicleRequestBean.Signature(
                "https://pagwapi.rentey.com/webapigw/Temp/Downloads/placeholder-signature.jpeg"
        );
        
        // Build reference details
        Integer checkTypeId = prepResult.checkType() != null && prepResult.checkType().id() != null ? 
                prepResult.checkType().id() : 6;
        ReceiveNewVehicleRequestBean.ReferenceDetails referenceDetails = 
                new ReceiveNewVehicleRequestBean.ReferenceDetails(checkTypeId);
        
        // Build skeleton details
        ReceiveNewVehicleRequestBean.SkeletonDetails skeletonDetails = null;
        if (prepResult.vehicleSkeletonDetails() != null) {
            GetVehicleCheckPreparationDataResponseBean.VehicleSkeletonDetails skeleton = 
                    prepResult.vehicleSkeletonDetails();
            if (skeleton.image() != null) {
                ReceiveNewVehicleRequestBean.ImageInfo skeletonImage = 
                        new ReceiveNewVehicleRequestBean.ImageInfo(
                                skeleton.image().id(),
                                skeleton.image().url(),
                                skeleton.image().isNewDocument()
                        );
                skeletonDetails = new ReceiveNewVehicleRequestBean.SkeletonDetails(
                        skeleton.id(),
                        skeletonImage
                );
            }
        }
        
        // Build vehicle check damages from checklist details
        List<ReceiveNewVehicleRequestBean.CheckItemStatus> checkItemStatuses = new ArrayList<>();
        if (prepResult.checklistDetails() != null) {
            for (GetVehicleCheckPreparationDataResponseBean.ChecklistDetail checklist : prepResult.checklistDetails()) {
                if (checklist.checkItems() != null) {
                    for (GetVehicleCheckPreparationDataResponseBean.CheckItem checkItem : checklist.checkItems()) {
                        // Use the first passed choice if available, otherwise first choice
                        Integer choiceId = null;
                        if (checkItem.passedChoices() != null && !checkItem.passedChoices().isEmpty()) {
                            choiceId = checkItem.passedChoices().get(0);
                        } else if (checkItem.choices() != null && !checkItem.choices().isEmpty()) {
                            choiceId = checkItem.choices().get(0).id();
                        }
                        
                        if (choiceId != null) {
                            checkItemStatuses.add(new ReceiveNewVehicleRequestBean.CheckItemStatus(
                                    checklist.id(),
                                    checkItem.id(),
                                    choiceId
                            ));
                        }
                    }
                }
            }
        }
        
        ReceiveNewVehicleRequestBean.VehicleCheckDamages vehicleCheckDamages = 
                new ReceiveNewVehicleRequestBean.VehicleCheckDamages(
                        checkItemStatuses,
                        new ArrayList<>(), // skeletonBodyDamages
                        new ArrayList<>()  // otherDamages
                );
        
        // Build snapshots from snapshot details list
        List<ReceiveNewVehicleRequestBean.SnapshotItem> snapshots = new ArrayList<>();
        if (prepResult.checkType() != null && prepResult.checkType().snapshotDetailsList() != null) {
            for (GetVehicleCheckPreparationDataResponseBean.SnapshotDetails snapshotDetail : 
                    prepResult.checkType().snapshotDetailsList()) {
                // Use placeholder URL for snapshots
                ReceiveNewVehicleRequestBean.SnapshotImage snapshotImage = 
                        new ReceiveNewVehicleRequestBean.SnapshotImage(
                                "https://pagwapi.rentey.com/webapigw/Temp/Downloads/placeholder-snapshot.jpeg"
                        );
                ReceiveNewVehicleRequestBean.Snapshot snapshot = 
                        new ReceiveNewVehicleRequestBean.Snapshot(snapshotImage);
                snapshots.add(new ReceiveNewVehicleRequestBean.SnapshotItem(
                        snapshotDetail.snapshotId(),
                        snapshot
                ));
            }
        }
        
        // Build total damages cost
        ReceiveNewVehicleRequestBean.Cost totalDamagesCost = 
                new ReceiveNewVehicleRequestBean.Cost(0.0, 1); // value: 0, currencyId: 1
        
        // Build the complete request
        return new ReceiveNewVehicleRequestBean(
                vehicleId,
                fuelId,
                odometer,
                signature,
                referenceDetails,
                skeletonDetails,
                vehicleCheckDamages,
                snapshots,
                totalDamagesCost,
                null // damageStatusId
        );
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

