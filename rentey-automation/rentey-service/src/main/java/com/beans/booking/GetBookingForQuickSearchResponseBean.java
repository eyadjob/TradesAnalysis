package com.beans.booking;

import com.beans.interfaces.ResponsePayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response bean for GetBookingForQuickSearch API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetBookingForQuickSearchResponseBean(
        @JsonProperty("result") BookingQuickSearchResult result,
        @JsonProperty("targetUrl") String targetUrl,
        @JsonProperty("success") Boolean success,
        @JsonProperty("error") Object error,
        @JsonProperty("unAuthorizedRequest") Boolean unAuthorizedRequest,
        @JsonProperty("__abp") Boolean abp
) implements ResponsePayload {
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingQuickSearchResult(
            @JsonProperty("bookingNo") String bookingNo,
            @JsonProperty("sourceId") Integer sourceId,
            @JsonProperty("recordActions") List<RecordAction> recordActions,
            @JsonProperty("durationTypeId") Integer durationTypeId,
            @JsonProperty("reassignVehicleReasonId") Integer reassignVehicleReasonId,
            @JsonProperty("countryId") Integer countryId,
            @JsonProperty("organizationId") Integer organizationId,
            @JsonProperty("isOnFlyRate") Boolean isOnFlyRate,
            @JsonProperty("statusId") Integer statusId,
            @JsonProperty("isOwnedCorporate") Boolean isOwnedCorporate,
            @JsonProperty("modeId") Integer modeId,
            @JsonProperty("pickupDate") String pickupDate,
            @JsonProperty("bookingTypeId") Integer bookingTypeId,
            @JsonProperty("vehicleId") Integer vehicleId,
            @JsonProperty("id") Integer id,
            @JsonProperty("pickupBranchId") Integer pickupBranchId
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

