package com.services;

import com.annotation.LogExecutionTime;
import com.beans.country.GetCountryCurrencyInfoResponseBean;
import com.beans.general.AbpResponseBean;
import com.beans.vehicle.*;
import com.enums.VehicleCheckTypes;
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
        CreateVehiclesResponseBean createVehiclesResponseBean = vehicleService.createVehicles(request);
        CreateVehiclesResponseBean.vehiclePlateNumber = randomPlateNumber;

        return createVehiclesResponseBean;
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

        CreateVehiclesResponseBean createResponse = createVehicleWithRandomPlateNumber(countryName, branchName);

        if (createResponse == null || !Boolean.TRUE.equals(createResponse.success())) {
            logger.error("Failed to create vehicle. Response: {}", createResponse);
            throw new RuntimeException("Failed to create vehicle: " + (createResponse != null ? createResponse.error() : "Unknown error"));
        }

        int countryId = Integer.parseInt(countryService.getOperationalCountryIdFromName(countryName));
        String branchId = countryService.getBranchIdByName(
                countryService.getUserBranchesForCombobox(countryId, new ArrayList<>(List.of(8900, 8902))),
                branchName);

        GetAllBranchVehiclesResponseBean vehiclesResponse = vehicleService.getAllBranchVehicles(countryId, Integer.parseInt(branchId), CreateVehiclesResponseBean.vehiclePlateNumber);
        GetAllBranchVehiclesResponseBean.BranchVehicle createdVehicle = vehiclesResponse.result().data().get(0);
        Integer vehicleId = createdVehicle.id();
        String plateNumber = createdVehicle.plateNo();
        logger.info("Found created vehicle with ID: {}, plate number: {}", vehicleId, plateNumber);

        Integer checkTypeId = VehicleCheckTypes.RECEIVE_VEHICLE.getId();
        Integer sourceId = 120;
        GetVehicleCheckPreparationDataResponseBean preparationData = vehicleService.getVehicleCheckPreparationData(vehicleId, checkTypeId, sourceId);

        if (preparationData == null || preparationData.result() == null) {
            logger.error("Failed to get vehicle check preparation data");
            throw new RuntimeException("Failed to get vehicle check preparation data");
        }
        GetVehicleCheckPreparationDataResponseBean.VehicleCheckPreparationDataResult prepResult =
                preparationData.result();

        ReceiveNewVehicleRequestBean receiveRequest = buildReceiveNewVehicleRequest(
                vehicleId,
                prepResult,
                createdVehicle,
                checkTypeId
        );

        // Step 6: Call receiveNewVehicle
        logger.info("Receiving new vehicle with ID: {}", vehicleId);
        return vehicleService.receiveNewVehicle(receiveRequest);
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
    public AbpResponseBean createAndReceiveNewVehicle(String countryName, String branchName,CreateVehiclesResponseBean createVehiclesResponse ) {
        logger.info("Creating and receiving new vehicle for country: {}, branch: {}", countryName, branchName);


        if (createVehiclesResponse == null || !Boolean.TRUE.equals(createVehiclesResponse.success())) {
            logger.error("Failed to create vehicle. Response: {}", createVehiclesResponse);
            throw new RuntimeException("Failed to create vehicle: " + (createVehiclesResponse != null ? createVehiclesResponse.error() : "Unknown error"));
        }

        int countryId = Integer.parseInt(countryService.getOperationalCountryIdFromName(countryName));
        String branchId = countryService.getBranchIdByName(
                countryService.getUserBranchesForCombobox(countryId, new ArrayList<>(List.of(8900, 8902))),
                branchName);

        GetAllBranchVehiclesResponseBean vehiclesResponse = vehicleService.getAllBranchVehicles(countryId, Integer.parseInt(branchId), CreateVehiclesResponseBean.vehiclePlateNumber);
        GetAllBranchVehiclesResponseBean.BranchVehicle createdVehicle = vehiclesResponse.result().data().get(0);
        Integer vehicleId = createdVehicle.id();
        String plateNumber = createdVehicle.plateNo();
        logger.info("Found created vehicle with ID: {}, plate number: {}", vehicleId, plateNumber);

        Integer checkTypeId = VehicleCheckTypes.RECEIVE_VEHICLE.getId();
        Integer sourceId = 120;
        GetVehicleCheckPreparationDataResponseBean preparationData = vehicleService.getVehicleCheckPreparationData(vehicleId, checkTypeId, sourceId);

        if (preparationData == null || preparationData.result() == null) {
            logger.error("Failed to get vehicle check preparation data");
            throw new RuntimeException("Failed to get vehicle check preparation data");
        }
        GetVehicleCheckPreparationDataResponseBean.VehicleCheckPreparationDataResult prepResult =
                preparationData.result();

        ReceiveNewVehicleRequestBean receiveRequest = buildReceiveNewVehicleRequest(
                vehicleId,
                prepResult,
                createdVehicle,
                checkTypeId
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
            GetAllBranchVehiclesResponseBean.BranchVehicle vehicle,
            Integer checkTypeId) {
        Integer fuelLevelId = Integer.parseInt(lookupsService.getComboboxItemValueByDisplayText(lookupsService.getAllItemsComboboxItems(12), vehicle.fuel()));
        Integer odometer = prepResult.odometer() != null ? prepResult.odometer() :
                (prepResult.minimumOdomter() != null ? prepResult.minimumOdomter() :
                        (vehicle.odometer() != null ? vehicle.odometer() : 22));

        String signatureUrl = vehicleService.uploadSignatureImage();

        // Build signature with uploaded image URL
        ReceiveNewVehicleRequestBean.Signature signature = new ReceiveNewVehicleRequestBean.Signature(signatureUrl);
        ReceiveNewVehicleRequestBean.ReferenceDetails referenceDetails =
                new ReceiveNewVehicleRequestBean.ReferenceDetails(checkTypeId);

        // Build skeleton details
        GetVehicleCheckPreparationDataResponseBean.VehicleSkeletonDetails skeleton = prepResult.vehicleSkeletonDetails();
        ReceiveNewVehicleRequestBean.ImageInfo skeletonImage = new ReceiveNewVehicleRequestBean.ImageInfo(
                skeleton.image().id(),
                skeleton.image().url(),
                skeleton.image().isNewDocument()
        );
        ReceiveNewVehicleRequestBean.SkeletonDetails skeletonDetails = new ReceiveNewVehicleRequestBean.SkeletonDetails(
                skeleton.id(),
                skeletonImage
        );

        List<ReceiveNewVehicleRequestBean.CheckItemStatus> checkItemStatuses = new ArrayList<>();
        for (GetVehicleCheckPreparationDataResponseBean.ChecklistDetail checklist : prepResult.checklistDetails()) {
            for (GetVehicleCheckPreparationDataResponseBean.CheckItem checkItem : checklist.checkItems()) {
                Integer choiceId = null;
                choiceId = checkItem.passedChoices().get(0);
                choiceId = checkItem.choices().get(0).id();

                if (choiceId != null) {
                    checkItemStatuses.add(new ReceiveNewVehicleRequestBean.CheckItemStatus(
                            checklist.id(),
                            checkItem.id(),
                            choiceId
                    ));
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
        for (GetVehicleCheckPreparationDataResponseBean.SnapshotDetails snapshotDetail :
                prepResult.checkType().snapshotDetailsList()) {
            // Use placeholder URL for snapshots
            ReceiveNewVehicleRequestBean.SnapshotImage snapshotImage =
                    new ReceiveNewVehicleRequestBean.SnapshotImage(
                            signatureUrl
                    );
            ReceiveNewVehicleRequestBean.Snapshot snapshot =
                    new ReceiveNewVehicleRequestBean.Snapshot(snapshotImage);
            snapshots.add(new ReceiveNewVehicleRequestBean.SnapshotItem(
                    snapshotDetail.snapshotId(),
                    snapshot
            ));
        }

        GetCountryCurrencyInfoResponseBean countryCurrencyInfo = countryService.getCountryCurrencyInfo(vehicle.countryId());
        // Build total damages cost
        ReceiveNewVehicleRequestBean.Cost totalDamagesCost =
                new ReceiveNewVehicleRequestBean.Cost(0.0, countryCurrencyInfo.result().id()); // value: 0, currencyId: 1

        // Build the complete request
        return new ReceiveNewVehicleRequestBean(
                vehicleId,
                fuelLevelId,
                odometer,
                signature,
                referenceDetails,
                skeletonDetails,
                vehicleCheckDamages,
                snapshots,
                totalDamagesCost,
                null
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

