package com.beans.contract;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request bean for ExecuteBooking API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExecuteBookingRequestBean(
        @JsonProperty("rentalRateId") Integer rentalRateId,
        @JsonProperty("contractPaymentInfo") ContractPaymentInfo contractPaymentInfo,
        @JsonProperty("userDiscounts") List<Object> userDiscounts,
        @JsonProperty("externalLoyaltyId") Integer externalLoyaltyId,
        @JsonProperty("contractOffers") List<Object> contractOffers,
        @JsonProperty("bookingId") String bookingId,
        @JsonProperty("calculationResult") CalculationResult calculationResult,
        @JsonProperty("pickupBranchId") Integer pickupBranchId,
        @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
        @JsonProperty("dropoffDate") String dropoffDate,
        @JsonProperty("source") Integer source,
        @JsonProperty("transferCostId") Integer transferCostId,
        @JsonProperty("extras") List<Object> extras,
        @JsonProperty("vehicleInfo") VehicleInfo vehicleInfo,
        @JsonProperty("customerInfo") CustomerInfo customerInfo,
        @JsonProperty("skipAuthorization") Boolean skipAuthorization,
        @JsonProperty("readyVehicleBlockingKey") String readyVehicleBlockingKey,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("comments") Object comments,
        @JsonProperty("couponCode") String couponCode,
        @JsonProperty("driverAuthorizationTypeId") Integer driverAuthorizationTypeId,
        @JsonProperty("vehicleCheckData") VehicleCheckData vehicleCheckData
) implements RequestPayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContractPaymentInfo(
            @JsonProperty("voucherCreateInputList") List<VoucherCreateInput> voucherCreateInputList
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VoucherCreateInput(
            @JsonProperty("referenceId") String referenceId,
            @JsonProperty("voucherOperationTypeId") Integer voucherOperationTypeId,
            @JsonProperty("voucherTypeId") Integer voucherTypeId,
            @JsonProperty("basePaymentInformationDto") BasePaymentInformationDto basePaymentInformationDto,
            @JsonProperty("voucherDateTime") String voucherDateTime,
            @JsonProperty("sourceId") Integer sourceId
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BasePaymentInformationDto(
            @JsonProperty("discriminator") String discriminator,
            @JsonProperty("paymentMethodId") Integer paymentMethodId,
            @JsonProperty("amount") Money amount
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Money(
            @JsonProperty("value") Double value,
            @JsonProperty("currencyId") Integer currencyId,
            @JsonProperty("isoCode") String isoCode
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CalculationResult(
            @JsonProperty("netAmount") Double netAmount,
            @JsonProperty("itemsSummary") Object itemsSummary,
            @JsonProperty("insuranceDeposit") Double insuranceDeposit,
            @JsonProperty("isAdditional") Boolean isAdditional,
            @JsonProperty("isSecretRate") Boolean isSecretRate,
            @JsonProperty("isCummlative") Boolean isCummlative,
            @JsonProperty("calculationItems") List<CalculationItem> calculationItems
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CalculationItem(
            @JsonProperty("itemTypeId") Integer itemTypeId,
            @JsonProperty("itemName") String itemName,
            @JsonProperty("itemNameLocalized") LocalizedString itemNameLocalized,
            @JsonProperty("itemNameRaw") String itemNameRaw,
            @JsonProperty("quantity") Double quantity,
            @JsonProperty("isPctDiscount") Boolean isPctDiscount,
            @JsonProperty("discountPCT") Double discountPCT,
            @JsonProperty("discountAmount") Double discountAmount,
            @JsonProperty("minimumDiscount") Double minimumDiscount,
            @JsonProperty("maximumDiscount") Double maximumDiscount,
            @JsonProperty("minimumDiscountAmount") Double minimumDiscountAmount,
            @JsonProperty("maximumDiscountAmount") Double maximumDiscountAmount,
            @JsonProperty("discountDescription") List<Object> discountDescription,
            @JsonProperty("taxPercentage") Double taxPercentage,
            @JsonProperty("taxAmount") Double taxAmount,
            @JsonProperty("unitPrice") Double unitPrice,
            @JsonProperty("totalPrice") Double totalPrice,
            @JsonProperty("netPrice") Double netPrice,
            @JsonProperty("isVisible") Boolean isVisible,
            @JsonProperty("unitDescriptionId") Integer unitDescriptionId,
            @JsonProperty("unitDescription") String unitDescription,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("taxConfigurations") String taxConfigurations,
            @JsonProperty("currencyFraction") Integer currencyFraction,
            @JsonProperty("discountCapAmount") Double discountCapAmount,
            @JsonProperty("originalDiscountPct") Double originalDiscountPct,
            @JsonProperty("isSecretRate") Boolean isSecretRate
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LocalizedString(
            @JsonProperty("stringValue") String stringValue,
            @JsonProperty("currentCultureText") String currentCultureText
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Duration(
            @JsonProperty("start") String start,
            @JsonProperty("end") String end
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleInfo(
            @JsonProperty("vehicleId") Integer vehicleId,
            @JsonProperty("categoryId") Integer categoryId,
            @JsonProperty("odometer") Integer odometer,
            @JsonProperty("fuelId") Integer fuelId,
            @JsonProperty("branchId") Integer branchId
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerInfo(
            @JsonProperty("driverId") Long driverId,
            @JsonProperty("identityId") Long identityId
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleCheckData(
            @JsonProperty("vehicleId") Integer vehicleId,
            @JsonProperty("fuelId") Integer fuelId,
            @JsonProperty("odometer") Integer odometer,
            @JsonProperty("signature") Signature signature,
            @JsonProperty("referenceDetails") ReferenceDetails referenceDetails,
            @JsonProperty("skeletonDetails") SkeletonDetails skeletonDetails,
            @JsonProperty("vehicleCheckDamages") VehicleCheckDamages vehicleCheckDamages,
            @JsonProperty("snapshots") List<Object> snapshots,
            @JsonProperty("totalDamagesCost") Money totalDamagesCost,
            @JsonProperty("damageStatusId") Integer damageStatusId,
            @JsonProperty("franchiseId") Integer franchiseId
    ) {
    }
    
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
            @JsonProperty("skeletonImage") SkeletonImage skeletonImage
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SkeletonImage(
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
}

