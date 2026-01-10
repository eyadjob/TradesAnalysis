package com.beans.vehicle;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response bean for GetBookingVehicles API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetBookingVehiclesResponseBean(
        @JsonProperty("result") BookingVehiclesResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingVehiclesResult(
            @JsonProperty("items") List<BookingVehicle> items
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingVehicle(
            @JsonProperty("plateNo") String plateNo,
            @JsonProperty("fuel") String fuel,
            @JsonProperty("fuelId") Integer fuelId,
            @JsonProperty("categoryId") Integer categoryId,
            @JsonProperty("modelId") Integer modelId,
            @JsonProperty("year") Integer year,
            @JsonProperty("odometer") Integer odometer,
            @JsonProperty("rentalRate") VehicleRentalRate rentalRate,
            @JsonProperty("categoryName") String categoryName,
            @JsonProperty("modelName") String modelName,
            @JsonProperty("trimId") Integer trimId,
            @JsonProperty("engineSize") Integer engineSize,
            @JsonProperty("branchId") Integer branchId,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("manufacturerName") String manufacturerName,
            @JsonProperty("currencyId") Integer currencyId,
            @JsonProperty("currencyIso") String currencyIso,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("colorId") Integer colorId,
            @JsonProperty("color") String color,
            @JsonProperty("colorHexaValue") String colorHexaValue,
            @JsonProperty("validations") VehicleValidations validations,
            @JsonProperty("oldRentalRate") VehicleRentalRate oldRentalRate,
            @JsonProperty("contractType") Integer contractType,
            @JsonProperty("minmumFuelLevelId") Integer minmumFuelLevelId,
            @JsonProperty("isFuelLevelRestircationAppliedOnContractType") Boolean isFuelLevelRestircationAppliedOnContractType,
            @JsonProperty("isVehicleNotAssignedToMaintenancePlanOfKMAndMonths") Boolean isVehicleNotAssignedToMaintenancePlanOfKMAndMonths,
            @JsonProperty("failedBookingUpgrade") Boolean failedBookingUpgrade,
            @JsonProperty("discountDescription") List<String> discountDescription,
            @JsonProperty("hasAdditionalStatus") Boolean hasAdditionalStatus,
            @JsonProperty("maintenancePlansIds") List<Integer> maintenancePlansIds,
            @JsonProperty("lastModificationTime") String lastModificationTime,
            @JsonProperty("lastModifierUserId") Long lastModifierUserId,
            @JsonProperty("creationTime") String creationTime,
            @JsonProperty("creatorUserId") Long creatorUserId,
            @JsonProperty("id") Integer id
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleRentalRate(
            @JsonProperty("rentalRateId") Integer rentalRateId,
            @JsonProperty("dailyRate") Double dailyRate,
            @JsonProperty("hourlyRate") Double hourlyRate,
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
    public record VehicleValidations(
            @JsonProperty("validations") List<Object> validations,
            @JsonProperty("canContinue") Boolean canContinue,
            @JsonProperty("hasErrors") Boolean hasErrors
    ) {
    }
}
