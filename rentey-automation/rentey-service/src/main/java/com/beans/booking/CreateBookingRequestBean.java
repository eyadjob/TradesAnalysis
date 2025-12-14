package com.beans.booking;

import com.beans.interfaces.RequestPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request bean for CreateBooking API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBookingRequestBean(
        @JsonProperty("countryId") Integer countryId,
        @JsonProperty("pickupDate") String pickupDate,
        @JsonProperty("dropoffDate") String dropoffDate,
        @JsonProperty("pickupBranchId") Integer pickupBranchId,
        @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
        @JsonProperty("categoryId") String categoryId,
        @JsonProperty("modelId") String modelId,
        @JsonProperty("year") Integer year,
        @JsonProperty("driverId") Integer driverId,
        @JsonProperty("rentalRateId") Integer rentalRateId,
        @JsonProperty("transferCostId") Integer transferCostId,
        @JsonProperty("sourceId") Integer sourceId,
        @JsonProperty("voucherCreateInputList") List<VoucherCreateInput> voucherCreateInputList,
        @JsonProperty("extras") List<Object> extras,
        @JsonProperty("couponCode") String couponCode,
        @JsonProperty("userDiscounts") List<Object> userDiscounts,
        @JsonProperty("bookingOffers") List<Object> bookingOffers
) implements RequestPayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VoucherCreateInput(
            @JsonProperty("voucherOperationTypeId") Integer voucherOperationTypeId,
            @JsonProperty("voucherTypeId") Integer voucherTypeId,
            @JsonProperty("basePaymentInformationDto") BasePaymentInformationDto basePaymentInformationDto,
            @JsonProperty("sourceId") Integer sourceId
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BasePaymentInformationDto(
            @JsonProperty("discriminator") String discriminator,
            @JsonProperty("paymentMethodId") Integer paymentMethodId,
            @JsonProperty("amount") Amount amount
    ) {
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Amount(
            @JsonProperty("value") Integer value,
            @JsonProperty("currencyId") Integer currencyId
    ) {
    }
}

