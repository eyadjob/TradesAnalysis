package com.beans.vehicle;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Request bean for CreateVehicles API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateVehiclesRequestBean(
        @JsonProperty("vehicleDtos") List<VehicleDto> vehicleDtos
) implements RequestPayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleDto(
            @JsonProperty("isBulkUploaded") Boolean isBulkUploaded,
            @JsonProperty("odometer") String odometer,
            @JsonProperty("fuelLevelId") Integer fuelLevelId,
            @JsonProperty("branchId") String branchId,
            @JsonProperty("vehicleManufacturingInfo") VehicleManufacturingInfo vehicleManufacturingInfo,
            @JsonProperty("vehicleLicenseInfo") VehicleLicenseInfo vehicleLicenseInfo,
            @JsonProperty("vehicleInsuranceInfo") VehicleInsuranceInfo vehicleInsuranceInfo,
            @JsonProperty("locationInfo") LocationInfo locationInfo,
            @JsonProperty("purchaseInfo") PurchaseInfo purchaseInfo,
            @JsonProperty("specs") VehicleSpecs specs,
            @JsonProperty("countryId") String countryId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleManufacturingInfo(
            @JsonProperty("modelId") String modelId,
            @JsonProperty("year") Integer year,
            @JsonProperty("chassisNo") String chassisNo
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleLicenseInfo(
            @JsonProperty("licenseTypeId") String licenseTypeId,
            @JsonProperty("usageTypeId") String usageTypeId,
            @JsonProperty("plateNo") String plateNo
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleInsuranceInfo(
            @JsonProperty("expiryDate") String expiryDate,
            @JsonProperty("number") String number,
            @JsonProperty("insuranceCompanyId") String insuranceCompanyId,
            @JsonProperty("accidentPolicyId") Integer accidentPolicyId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LocationInfo(
            @JsonProperty("currentLocationId") String currentLocationId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PurchaseInfo(
            @JsonProperty("vendorId") String vendorId,
            @JsonProperty("date") OffsetDateTime date,
            @JsonProperty("price") String price
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleSpecs(
            @JsonProperty("colorId") String colorId,
            @JsonProperty("trimLevelId") String trimLevelId,
            @JsonProperty("fuelTypeId") String fuelTypeId,
            @JsonProperty("fuelTankSize") Integer fuelTankSize,
            @JsonProperty("engineSize") Integer engineSize
    ) {
    }
}

