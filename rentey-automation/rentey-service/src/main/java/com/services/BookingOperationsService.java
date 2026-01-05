package com.services;

import com.beans.booking.*;
import com.beans.customer.CreateOrUpdateCustomerResponseBean;
import com.builders.CreateBookingRequestBuilder;
import com.enums.LookupTypes;
import com.pojo.CreateBookingResponseWrapper;
import com.util.DateUtil;
import com.util.PropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for building booking-related request beans and orchestrating booking creation operations.
 */
@Service
public class BookingOperationsService {

    private static final Map<String, String> userDefinedVariables = PropertyManager.loadPropertyFileIntoMap("user-defined-variables.properties");

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private LookupsService lookupsService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CustomerOperationsService customerOperationsService;

    @Autowired
    private SettingsService settingsService;

    /**
     * Build CreateBookingRequestBean by orchestrating multiple API calls and building the request.
     *
     * @param countryName The country name.
     * @param branchName The branch name.
     * @return CreateBookingRequestBean instance.
     */
    public CreateBookingResponseWrapper CreateBookingWithNewCustomerAndNewVehicle(String countryName, String branchName) {
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String branchId = countryService.getBranchIdByName(countryId, branchName);
        countryService.getCountrySettings(Integer.parseInt(countryId), countryService.buildKeysForSettingsToGet("keys=App.CountryManagement.MinimumHoursToBooking&keys=App.CountryManagement.MinimumHoursToBrokerBooking&keys=App.CountryManagement.EnablePaymentOnSystemBooking&keys=App.CountryManagement.MaximumHoursToExecuteImmediateBooking&keys=App.CountryManagement.EnableExternalAuthorizationOnBooking&keys=App.CountryManagement.ContractMinimumHours&keys=App.CountryManagement.MaxDaysWhenAddContract&keys=App.CountryManagement.FreeHours&keys=App.CountryManagement.EnableFuelCost&keys=App.CountryManagement.MaxOdometerChange&keys=App.CountryManagement.MediumMaxAmount&keys=App.CountryManagement.ApplyExternalDriverAuthorizationOn"));
        contractService.getExtrasNamesExcludedFromBookingPaymentDetails();
        contractService.getCountriesPhone();
        lookupsService.getLookupItemIdByLookupTypeIdAndItemDisplayName(62, "Saudi Citizen");
        lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.MARITAL_STATUSES, "Single");
        lookupsService.getLookupItemIdByLookupTypeIdAndItemDisplayName(6, "Male");
        lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.VIP_LEVELS, "Level One");
        lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.LEVEL, "Father");
        lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.CREDIT_CARD_TYPES, "Visa");
        lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.BANK_NAMES, "Visa");
        lookupsService.getItemsByType(266, false);
        var createBookingDateInputs = bookingService.getCreateBookingDateInputs(Integer.parseInt(countryId));

        String pickupDate = createBookingDateInputs.result().minimumPickupDate();
        String dropOffDate = DateUtil.addTimeToDate(pickupDate,1,0,0,0);

        ValidateDurationAndLocationsRequestBean validateRequest = buildValidateDurationAndLocationRequest(
                Integer.parseInt(countryId),
                Integer.parseInt(branchId),
                Integer.parseInt(branchId),
                pickupDate,
                dropOffDate,
                6102,
                true,
                null,
                null
        );

        bookingService.validateDurationAndLocations(
                validateRequest,
                pickupDate,
                dropOffDate
        );

        GetBranchAvailableModelsForBookingComboboxItemsRequestBean availableModelsRequest = buildGetAvailableModelsRequest(
                Integer.parseInt(branchId),
                pickupDate,
                dropOffDate,
                "120",
                -1,
                -1,
                -1
        );

        vehicleService.getCarModelIdByName(
                vehicleService.getAllCarModels(),
                userDefinedVariables.get("automationCarModelName"));

        bookingService.getBranchAvailableModelsForBookingComboboxItems(availableModelsRequest);
        String vehicleModelId = bookingService.getModelIdByModelName(availableModelsRequest, userDefinedVariables.get("automationCarModelName"));
        String vehicleCategoryId = bookingService.getCategoryIddByCategoryName(availableModelsRequest, userDefinedVariables.get("automationCarCategoryName"));
        CreateOrUpdateCustomerResponseBean customerResponseBean = customerOperationsService.createCustomerWithRandomData(countryName);
        bookingService.validatePreventRentingRestriction(customerResponseBean.result().id(), Integer.parseInt(branchId), Integer.parseInt(vehicleModelId), pickupDate);
        bookingService.getCustomerContractInformationByName(customerResponseBean.result().fullName().displayName());
        int rentalSchemaPeriodId = settingsService.getRentalSchemaIdByNameAndByPeriodTypeName(Integer.parseInt(countryId), userDefinedVariables.get("automationRentalRateSchemaName"), "Daily");
        contractService.getIntegratedLoyaltiesFromLoyaltyApi();
        contractService.getExternalLoyaltiesConfigurationsItemsFromLoyaltyApi(false);
        contractService.getExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApi(customerResponseBean.result().id(), Integer.parseInt(branchId));
//        contractService.getContractExtraItems(Integer.valueOf(branchId), Integer.valueOf(vehicleCategoryId), rentalSchemaPeriodId, 1801, 230, 120, false, 32100);
        lookupsService.getPaymentMethodsComboboxItems(false, false);
        GetBestRentalRateForModelResponseBean bestRentalRateResponse = bookingService.getBestRentalRateForModel(Integer.valueOf(countryId), Integer.valueOf(branchId), Integer.valueOf(vehicleModelId), Integer.valueOf(userDefinedVariables.get("automationYear")), pickupDate, dropOffDate);
        lookupsService.getComboboxItemsValueByDisplayText("Card", 19);

        // Build calculate billing information request using data from previous responses
        CalculateBillingInformationRequestBean calculateBillingInformationRequest = buildCalculateBillingInformationRequest(
                new ArrayList<>(),
                new ArrayList<>(),
                vehicleModelId,
                Integer.valueOf(userDefinedVariables.get("automationYear")),
                null,
                bestRentalRateResponse.result().rentalRateId(),
                dropOffDate,
                pickupDate,
                210,
                2306,
                Integer.parseInt(branchId),
                Integer.parseInt(branchId),
                customerResponseBean.result().id().intValue(),
                vehicleCategoryId,
                ""
        );
        bookingService.calculateBillingInformation(calculateBillingInformationRequest);

        // Build create booking request using data from previous responses
        CreateBookingRequestBuilder builder = CreateBookingRequestBuilder.create();

        // Create Amount for payment
        CreateBookingRequestBean.Amount amount = builder.createAmount(0, 1);

        // Create BasePaymentInformationDto
        CreateBookingRequestBean.BasePaymentInformationDto basePaymentInfo = builder.createBasePaymentInformationDto(
                "CashPaymentInformationDto",
                290,
                amount
        );

        // Build the complete request
        CreateBookingRequestBean createBookingRequest = builder
                .withCountryId(Integer.parseInt(countryId))
                .withPickupDate(pickupDate)
                .withDropoffDate(dropOffDate)
                .withPickupBranchId(Integer.parseInt(branchId))
                .withDropoffBranchId(Integer.parseInt(branchId))
                .withCategoryId(vehicleCategoryId)
                .withModelId(vehicleModelId)
                .withYear(Integer.valueOf(userDefinedVariables.get("automationYear")))
                .withDriverId(customerResponseBean.result().id().intValue())
                .withRentalRateId(bestRentalRateResponse.result().rentalRateId())
                .withTransferCostId(null)
                .withSourceId(120)
                .withVoucherCreateInput(
                        2306,
                        270,
                        basePaymentInfo,
                        120
                )
                .withExtras(new ArrayList<>())
                .withCouponCode("")
                .withUserDiscounts(new ArrayList<>())
                .withBookingOffers(new ArrayList<>())
                .build();
        CreateBookingResponseBean createBookingResponseBean= bookingService.createBooking(createBookingRequest);
        CreateBookingResponseWrapper createBookingResponseWrapper = new CreateBookingResponseWrapper(createBookingResponseBean,createBookingRequest);
        return createBookingResponseWrapper;
    }

    /**
     * Build ValidateDurationAndLocationsRequestBean with provided parameters.
     *
     * @param branchCountryId The country ID for the branch.
     * @param pickupBranchId The pickup branch ID.
     * @param dropoffBranchId The dropoff branch ID.
     * @param pickupDate The pickup date (format: yyyy-MM-dd HH:mm:ss).
     * @param dropOffDate The dropoff date (format: yyyy-MM-dd HH:mm:ss).
     * @param bookingType The booking type ID.
     * @param validatePickUpDate Whether to validate pickup date.
     * @param rentalRateSchemaPeriodId The rental rate schema period ID (optional).
     * @param contractDuration The contract duration (optional).
     * @return ValidateDurationAndLocationsRequestBean instance.
     */
    public ValidateDurationAndLocationsRequestBean buildValidateDurationAndLocationRequest(
            Integer branchCountryId,
            Integer pickupBranchId,
            Integer dropoffBranchId,
            String pickupDate,
            String dropOffDate,
            Integer bookingType,
            Boolean validatePickUpDate,
            Integer rentalRateSchemaPeriodId,
            Integer contractDuration) {
        return new ValidateDurationAndLocationsRequestBean(
                branchCountryId,
                pickupBranchId,
                dropoffBranchId,
                pickupDate,
                dropOffDate,
                bookingType,
                validatePickUpDate,
                rentalRateSchemaPeriodId,
                contractDuration
        );
    }

    /**
     * Build GetBranchAvailableModelsForBookingComboboxItemsRequestBean with provided parameters.
     *
     * @param branchId The branch ID.
     * @param pickupDate The pickup date (format: yyyy-MM-dd HH:mm:ss).
     * @param dropOffDate The dropoff date (format: yyyy-MM-dd HH:mm:ss).
     * @param source The source ID as a string.
     * @param bookingCategoryId The booking category ID (use -1 for "Not assigned").
     * @param bookingModelId The booking model ID (use -1 for "Not assigned").
     * @param bookingVehicleYear The booking vehicle year (use -1 for "Not assigned").
     * @return GetBranchAvailableModelsForBookingComboboxItemsRequestBean instance.
     */
    public GetBranchAvailableModelsForBookingComboboxItemsRequestBean buildGetAvailableModelsRequest(
            Integer branchId,
            String pickupDate,
            String dropOffDate,
            String source,
            Integer bookingCategoryId,
            Integer bookingModelId,
            Integer bookingVehicleYear) {
        return new GetBranchAvailableModelsForBookingComboboxItemsRequestBean(
                branchId,
                pickupDate,
                dropOffDate,
                source,
                bookingCategoryId,
                bookingModelId,
                bookingVehicleYear
        );
    }

    /**
     * Build CalculateBillingInformationRequestBean with provided parameters.
     *
     * @param extras List of extras (optional, can be empty).
     * @param userDiscounts List of user discounts (optional, can be empty).
     * @param modelId The model ID (required).
     * @param year The year (required).
     * @param vehicleId The vehicle ID (optional, can be null).
     * @param rentalRateId The rental rate ID (required).
     * @param dropoffDate The dropoff date (required, format: yyyy-MM-dd HH:mm:ss).
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @param statusId The status ID (required).
     * @param voucherOperationTypeId The voucher operation type ID (required).
     * @param pickupBranchId The pickup branch ID (required).
     * @param dropoffBranchId The dropoff branch ID (required).
     * @param customerId The customer ID (required).
     * @param categoryId The category ID (required).
     * @param couponCode The coupon code (optional, can be empty string).
     * @return CalculateBillingInformationRequestBean instance.
     */
    public CalculateBillingInformationRequestBean buildCalculateBillingInformationRequest(
            List<Object> extras,
            List<Object> userDiscounts,
            String modelId,
            Integer year,
            Integer vehicleId,
            Integer rentalRateId,
            String dropoffDate,
            String pickupDate,
            Integer statusId,
            Integer voucherOperationTypeId,
            Integer pickupBranchId,
            Integer dropoffBranchId,
            Integer customerId,
            String categoryId,
            String couponCode) {
        return new CalculateBillingInformationRequestBean(
                extras != null ? extras : new ArrayList<>(),
                userDiscounts != null ? userDiscounts : new ArrayList<>(),
                modelId,
                year,
                vehicleId,
                rentalRateId,
                dropoffDate,
                pickupDate,
                statusId,
                voucherOperationTypeId,
                pickupBranchId,
                dropoffBranchId,
                customerId,
                categoryId,
                couponCode != null ? couponCode : ""
        );
    }
}
