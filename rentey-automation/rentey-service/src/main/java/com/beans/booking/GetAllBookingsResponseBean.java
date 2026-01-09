package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Response bean for GetAllBookings API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetAllBookingsResponseBean(
        @JsonProperty("result") BookingsResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingsResult(
            @JsonProperty("total") Integer total,
            @JsonProperty("data") List<BookingItem> data
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingItem(
            @JsonProperty("pickupBranch") String pickupBranch,
            @JsonProperty("sourceId") Integer sourceId,
            @JsonProperty("modeName") String modeName,
            @JsonProperty("creatorUserId") Integer creatorUserId,
            @JsonProperty("customerDisplayName") String customerDisplayName,
            @JsonProperty("creationTime") OffsetDateTime creationTime,
            @JsonProperty("lastModificationTime") OffsetDateTime lastModificationTime,
            @JsonProperty("customerPrimaryPhone") String customerPrimaryPhone,
            @JsonProperty("source") String source,
            @JsonProperty("recordActions") List<RecordAction> recordActions,
            @JsonProperty("durationTypeId") Integer durationTypeId,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("organizationId") Integer organizationId,
            @JsonProperty("dropoffBranchId") Integer dropoffBranchId,
            @JsonProperty("modeId") Integer modeId,
            @JsonProperty("isLiteCustomer") Boolean isLiteCustomer,
            @JsonProperty("customerEmail") String customerEmail,
            @JsonProperty("bookingStatus") String bookingStatus,
            @JsonProperty("franchiseId") Integer franchiseId,
            @JsonProperty("customerMobileNumber") String customerMobileNumber,
            @JsonProperty("modifiedBy") String modifiedBy,
            @JsonProperty("bookingTypeId") Integer bookingTypeId,
            @JsonProperty("vehicleId") Integer vehicleId,
            @JsonProperty("id") Integer id,
            @JsonProperty("balancePayment") Double balancePayment,
            @JsonProperty("brand") String brand,
            @JsonProperty("pickupBranchId") Integer pickupBranchId,
            @JsonProperty("organizationName") String organizationName,
            @JsonProperty("plateNo") String plateNo,
            @JsonProperty("durationTypeName") String durationTypeName,
            @JsonProperty("lastModifierUserId") Integer lastModifierUserId,
            @JsonProperty("dropoffBranch") String dropoffBranch,
            @JsonProperty("reassignVehicleReasonId") Integer reassignVehicleReasonId,
            @JsonProperty("customerName") String customerName,
            @JsonProperty("isOnFlyRate") Boolean isOnFlyRate,
            @JsonProperty("vehicleDescription") String vehicleDescription,
            @JsonProperty("statusId") Integer statusId,
            @JsonProperty("driverId") Long driverId,
            @JsonProperty("isOwnedCorporate") Boolean isOwnedCorporate,
            @JsonProperty("createdBy") String createdBy,
            @JsonProperty("dropoffDate") String dropoffDate,
            @JsonProperty("brokerBookingReferenceNumber") String brokerBookingReferenceNumber,
            @JsonProperty("identityNumbers") String identityNumbers,
            @JsonProperty("pickupDate") String pickupDate,
            @JsonProperty("bookingTypeName") String bookingTypeName,
            @JsonProperty("paymentTerm") String paymentTerm,
            @JsonProperty("bookingNumber") String bookingNumber
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RecordAction(
            @JsonProperty("orderNumber") Integer orderNumber,
            @JsonProperty("showAction") Boolean showAction,
            @JsonProperty("permissions") Object permissions,
            @JsonProperty("icon") String icon,
            @JsonProperty("errorMessage") String errorMessage,
            @JsonProperty("customClass") String customClass,
            @JsonProperty("frontEndFunctionParameter") String frontEndFunctionParameter,
            @JsonProperty("frontEndFunction") String frontEndFunction,
            @JsonProperty("hasError") Boolean hasError,
            @JsonProperty("title") String title
    ) {
    }
}

