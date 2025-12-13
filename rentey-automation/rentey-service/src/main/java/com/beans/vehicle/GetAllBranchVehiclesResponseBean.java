package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Response bean for GetAllBranchVehicles API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetAllBranchVehiclesResponseBean(
        @JsonProperty("result") BranchVehiclesResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    public record BranchVehiclesResult(
            @JsonProperty("total") Integer total,
            @JsonProperty("data") List<BranchVehicle> data
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BranchVehicle(
            @JsonProperty("category") String category,
            @JsonProperty("manufacturer") String manufacturer,
            @JsonProperty("model") String model,
            @JsonProperty("year") Integer year,
            @JsonProperty("plateNo") String plateNo,
            @JsonProperty("status") String status,
            @JsonProperty("statusId") Integer statusId,
            @JsonProperty("additionalStatuses") String additionalStatuses,
            @JsonProperty("additionalStatusIds") List<Integer> additionalStatusIds,
            @JsonProperty("usageType") String usageType,
            @JsonProperty("vendor") String vendor,
            @JsonProperty("branch") String branch,
            @JsonProperty("currentLocationId") Integer currentLocationId,
            @JsonProperty("currentLocation") String currentLocation,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("branchId") Integer branchId,
            @JsonProperty("dailyRentalRate") Double dailyRentalRate,
            @JsonProperty("isNeedCleaning") Boolean isNeedCleaning,
            @JsonProperty("isAssignVehicleToBranch") Boolean isAssignVehicleToBranch,
            @JsonProperty("isAllowedToChangeVehicleLocation") Boolean isAllowedToChangeVehicleLocation,
            @JsonProperty("usageTypeId") Integer usageTypeId,
            @JsonProperty("engineSize") Integer engineSize,
            @JsonProperty("trimId") Integer trimId,
            @JsonProperty("modelId") Integer modelId,
            @JsonProperty("needToBeChecked") Boolean needToBeChecked,
            @JsonProperty("isExternal") Boolean isExternal,
            @JsonProperty("odometer") Integer odometer,
            @JsonProperty("hasPreventiveMaintenance") Boolean hasPreventiveMaintenance,
            @JsonProperty("rentalRate") RentalRate rentalRate,
            @JsonProperty("categoryId") Integer categoryId,
            @JsonProperty("discountDescription") List<String> discountDescription,
            @JsonProperty("authorizationStatus") Boolean authorizationStatus,
            @JsonProperty("fuelId") Integer fuelId,
            @JsonProperty("fuel") String fuel,
            @JsonProperty("isSelfContractVehicleNeedsToBeChecked") Boolean isSelfContractVehicleNeedsToBeChecked,
            @JsonProperty("franchiseId") Integer franchiseId,
            @JsonProperty("recordActions") List<RecordAction> recordActions,
            @JsonProperty("vehicleOwner") String vehicleOwner,
            @JsonProperty("vehicleGuid") String vehicleGuid,
            @JsonProperty("isVehicleIntegratedWithTracking") Boolean isVehicleIntegratedWithTracking,
            @JsonProperty("fuelIntegrationStatusId") Integer fuelIntegrationStatusId,
            @JsonProperty("fuelingIntegrationStatusName") String fuelingIntegrationStatusName,
            @JsonProperty("isfuelingStatusInconsistent") Boolean isfuelingStatusInconsistent,
            @JsonProperty("lastModificationTime") OffsetDateTime lastModificationTime,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("creationTime") OffsetDateTime creationTime,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("id") Integer id
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RentalRate(
            @JsonProperty("rentalRateId") Integer rentalRateId,
            @JsonProperty("dailyRate") Double dailyRate,
            @JsonProperty("originalDailyRate") Double originalDailyRate,
            @JsonProperty("extraKMRate") Double extraKMRate,
            @JsonProperty("periodName") String periodName,
            @JsonProperty("periodId") Integer periodId,
            @JsonProperty("freeKM") Integer freeKM,
            @JsonProperty("currencyIso") String currencyIso,
            @JsonProperty("utilizationRateDiscountPercentage") Double utilizationRateDiscountPercentage,
            @JsonProperty("utilizationRateId") Integer utilizationRateId,
            @JsonProperty("hasOffers") Boolean hasOffers,
            @JsonProperty("hasUtilizationRateOrOffers") Boolean hasUtilizationRateOrOffers,
            @JsonProperty("currencyId") Integer currencyId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RecordAction(
            @JsonProperty("title") String title,
            @JsonProperty("icon") String icon,
            @JsonProperty("customClass") String customClass,
            @JsonProperty("frontEndFunction") String frontEndFunction,
            @JsonProperty("frontEndFunctionParameter") String frontEndFunctionParameter,
            @JsonProperty("orderNumber") Integer orderNumber,
            @JsonProperty("permissions") Object permissions,
            @JsonProperty("hasError") Boolean hasError,
            @JsonProperty("errorMessage") String errorMessage,
            @JsonProperty("showAction") Boolean showAction
    ) {
    }
}

