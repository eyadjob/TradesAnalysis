package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for CalculateBillingInformation API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CalculateBillingInformationResponseBean(
        @JsonProperty("result") BillingInformationResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BillingInformationResult(
            @JsonProperty("paymentDetailsDto") PaymentDetailsDto paymentDetailsDto,
            @JsonProperty("amountWithoutFullPaid") Double amountWithoutFullPaid,
            @JsonProperty("appliedOffers") List<Object> appliedOffers,
            @JsonProperty("vehicleInsuranceDetailsDto") VehicleInsuranceDetailsDto vehicleInsuranceDetailsDto
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PaymentDetailsDto(
            @JsonProperty("calculationResult") CalculationResult calculationResult,
            @JsonProperty("paymentInfo") PaymentInfo paymentInfo,
            @JsonProperty("voucherBasicInfo") VoucherBasicInfo voucherBasicInfo,
            @JsonProperty("transferCostId") Integer transferCostId,
            @JsonProperty("disablePayment") Boolean disablePayment,
            @JsonProperty("comments") String comments,
            @JsonProperty("refundDetails") Object refundDetails,
            @JsonProperty("redemptionVouchersAmount") Double redemptionVouchersAmount,
            @JsonProperty("damages") Object damages,
            @JsonProperty("rentalRestrictionsApplied") Object rentalRestrictionsApplied
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CalculationResult(
            @JsonProperty("netAmount") Double netAmount,
            @JsonProperty("insuranceDeposit") Double insuranceDeposit,
            @JsonProperty("isCummlative") Boolean isCummlative,
            @JsonProperty("isAdditional") Boolean isAdditional,
            @JsonProperty("isSecretRate") Boolean isSecretRate,
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
            @JsonProperty("unitDescription") String unitDescription,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("taxConfigurations") String taxConfigurations,
            @JsonProperty("currencyFraction") Integer currencyFraction,
            @JsonProperty("discountCapAmount") Double discountCapAmount,
            @JsonProperty("originalDiscountPct") Double originalDiscountPct,
            @JsonProperty("itemId") String itemId,
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
            @JsonProperty("end") String end,
            @JsonProperty("duration") String duration
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PaymentInfo(
            @JsonProperty("paidAmount") Double paidAmount,
            @JsonProperty("remainingAmount") Double remainingAmount,
            @JsonProperty("refundAmount") Double refundAmount,
            @JsonProperty("minimumPayment") Double minimumPayment,
            @JsonProperty("maximumPayment") Double maximumPayment,
            @JsonProperty("minimumPaymentPercentage") Double minimumPaymentPercentage,
            @JsonProperty("maximumPaymentPercentage") Double maximumPaymentPercentage,
            @JsonProperty("defaultPayment") Double defaultPayment,
            @JsonProperty("currencyIso") String currencyIso,
            @JsonProperty("currencyId") Integer currencyId,
            @JsonProperty("currencyDecimalPlaces") Integer currencyDecimalPlaces,
            @JsonProperty("voucherAmount") Double voucherAmount,
            @JsonProperty("nonDeferredAmount") Double nonDeferredAmount,
            @JsonProperty("maximumWalletPayment") Double maximumWalletPayment,
            @JsonProperty("brokerCoupon") Double brokerCoupon
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VoucherBasicInfo(
            @JsonProperty("typeName") String typeName,
            @JsonProperty("typeId") Integer typeId
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VehicleInsuranceDetailsDto(
            @JsonProperty("accidentCost") Double accidentCost,
            @JsonProperty("robberyCost") Double robberyCost,
            @JsonProperty("hitStillObjectCost") Double hitStillObjectCost,
            @JsonProperty("hitAndRunCost") Double hitAndRunCost
    ) {
    }
}

