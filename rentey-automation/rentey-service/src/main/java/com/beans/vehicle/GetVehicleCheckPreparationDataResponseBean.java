package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetVehicleCheckPreparationData API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetVehicleCheckPreparationDataResponseBean(
        @JsonProperty("result") VehicleCheckPreparationDataResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleCheckPreparationDataResult(
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("fuelId") Integer fuelId,
            @JsonProperty("currentBranchId") Integer currentBranchId,
            @JsonProperty("currentBranchName") String currentBranchName,
            @JsonProperty("odometer") Integer odometer,
            @JsonProperty("minimumOdomter") Integer minimumOdomter,
            @JsonProperty("maximumOdomter") Integer maximumOdomter,
            @JsonProperty("checkType") CheckType checkType,
            @JsonProperty("checklistDetails") List<ChecklistDetail> checklistDetails,
            @JsonProperty("latestVehicleCheckListStatus") List<Object> latestVehicleCheckListStatus,
            @JsonProperty("vehicleSkeletonDetails") VehicleSkeletonDetails vehicleSkeletonDetails,
            @JsonProperty("skeletonBodiesWithTheirRepairTasks") List<SkeletonBodyWithRepairTask> skeletonBodiesWithTheirRepairTasks,
            @JsonProperty("definedDamageTypes") List<DefinedDamageType> definedDamageTypes,
            @JsonProperty("damageStatusId") Integer damageStatusId,
            @JsonProperty("vehicleSummary") VehicleSummary vehicleSummary,
            @JsonProperty("lastECheckId") Integer lastECheckId,
            @JsonProperty("disableDamageStatus") Boolean disableDamageStatus,
            @JsonProperty("isTestingMode") Boolean isTestingMode,
            @JsonProperty("franchiseId") Integer franchiseId,
            @JsonProperty("contractTypeId") Integer contractTypeId,
            @JsonProperty("contractModeId") Integer contractModeId,
            @JsonProperty("isIntegratedWithTracking") Boolean isIntegratedWithTracking,
            @JsonProperty("vehicleGuid") String vehicleGuid,
            @JsonProperty("damageEstimation") Object damageEstimation,
            @JsonProperty("contractNumberForRead") String contractNumberForRead
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CheckType(
            @JsonProperty("id") Integer id,
            @JsonProperty("referenceTypeId") Integer referenceTypeId,
            @JsonProperty("name") String name,
            @JsonProperty("configurationDetails") ConfigurationDetails configurationDetails,
            @JsonProperty("snapshotDetailsList") List<SnapshotDetails> snapshotDetailsList,
            @JsonProperty("damageResponsibles") List<DamageResponsible> damageResponsibles,
            @JsonProperty("damageStatuses") List<DamageStatus> damageStatuses,
            @JsonProperty("notifiedEmailsUponDamage") String notifiedEmailsUponDamage,
            @JsonProperty("directionId") Integer directionId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ConfigurationDetails(
            @JsonProperty("isKMRequired") Boolean isKMRequired,
            @JsonProperty("isFuelRequired") Boolean isFuelRequired,
            @JsonProperty("hasSignature") Boolean hasSignature,
            @JsonProperty("allowRemoveDamage") Boolean allowRemoveDamage,
            @JsonProperty("allowAddDamage") Boolean allowAddDamage
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SnapshotDetails(
            @JsonProperty("snapshotName") String snapshotName,
            @JsonProperty("snapshotId") Integer snapshotId,
            @JsonProperty("isRequired") Boolean isRequired,
            @JsonProperty("snapshotMonth") Integer snapshotMonth,
            @JsonProperty("snapshotYear") Integer snapshotYear,
            @JsonProperty("image") Object image
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DamageResponsible(
            @JsonProperty("damageResponsibleName") String damageResponsibleName,
            @JsonProperty("damageResponsibleId") Integer damageResponsibleId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DamageStatus(
            @JsonProperty("damageStatusName") String damageStatusName,
            @JsonProperty("damageStatusId") Integer damageStatusId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ChecklistDetail(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("checkItems") List<CheckItem> checkItems
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CheckItem(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("passedChoices") List<Integer> passedChoices,
            @JsonProperty("choices") List<Choice> choices,
            @JsonProperty("repairTasks") List<RepairTask> repairTasks
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RepairTask(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("isRepeated") Boolean isRepeated,
            @JsonProperty("cost") Cost cost
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cost(
            @JsonProperty("value") Double value,
            @JsonProperty("currencyId") Integer currencyId,
            @JsonProperty("isoCode") String isoCode
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleSkeletonDetails(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("image") ImageInfo image,
            @JsonProperty("skeletonBase64string") String skeletonBase64string
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ImageInfo(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("isNewDocument") Boolean isNewDocument
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SkeletonBodyWithRepairTask(
            @JsonProperty("skeletonBodyId") Integer skeletonBodyId,
            @JsonProperty("skeletonBodyColors") List<Integer> skeletonBodyColors,
            @JsonProperty("damageId") Integer damageId,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("damageName") String damageName,
            @JsonProperty("repairTask") RepairTask repairTask
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DefinedDamageType(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("damageIcon") String damageIcon
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleSummary(
            @JsonProperty("statusId") Integer statusId,
            @JsonProperty("plateNumber") String plateNumber,
            @JsonProperty("category") String category,
            @JsonProperty("model") String model,
            @JsonProperty("manufactureYear") Integer manufactureYear,
            @JsonProperty("additionalStatus") List<Object> additionalStatus,
            @JsonProperty("usageTypeId") Integer usageTypeId
    ) {
    }
}

