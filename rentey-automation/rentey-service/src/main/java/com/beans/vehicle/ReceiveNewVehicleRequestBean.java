package com.beans.vehicle;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request bean for ReceiveNewVehicle API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReceiveNewVehicleRequestBean(
        @JsonProperty("vehicleId") Integer vehicleId,
        @JsonProperty("fuelId") Integer fuelId,
        @JsonProperty("odometer") Integer odometer,
        @JsonProperty("signature") Signature signature,
        @JsonProperty("referenceDetails") ReferenceDetails referenceDetails,
        @JsonProperty("skeletonDetails") SkeletonDetails skeletonDetails,
        @JsonProperty("vehicleCheckDamages") VehicleCheckDamages vehicleCheckDamages,
        @JsonProperty("snapshots") List<SnapshotItem> snapshots,
        @JsonProperty("totalDamagesCost") Cost totalDamagesCost,
        @JsonProperty("damageStatusId") Integer damageStatusId
) implements RequestPayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Signature(
            @JsonProperty("url") String url
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ReferenceDetails(
            @JsonProperty("checkTypeId") Integer checkTypeId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SkeletonDetails(
            @JsonProperty("skeletonId") Integer skeletonId,
            @JsonProperty("skeletonImage") ImageInfo skeletonImage
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
    public record VehicleCheckDamages(
            @JsonProperty("checkItemStatuses") List<CheckItemStatus> checkItemStatuses,
            @JsonProperty("skeletonBodyDamages") List<Object> skeletonBodyDamages,
            @JsonProperty("otherDamages") List<Object> otherDamages
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CheckItemStatus(
            @JsonProperty("checklistId") Integer checklistId,
            @JsonProperty("checkItemId") Integer checkItemId,
            @JsonProperty("choiceId") Integer choiceId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SnapshotItem(
            @JsonProperty("snapshotId") Integer snapshotId,
            @JsonProperty("snapshot") Snapshot snapshot
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Snapshot(
            @JsonProperty("image") SnapshotImage image
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SnapshotImage(
            @JsonProperty("url") String url
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cost(
            @JsonProperty("value") Double value,
            @JsonProperty("currencyId") Integer currencyId
    ) {
    }
}

