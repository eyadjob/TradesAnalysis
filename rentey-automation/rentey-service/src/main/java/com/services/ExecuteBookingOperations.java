package com.services;

import com.annotation.LogExecutionTime;
import com.beans.booking.CreateBookingResponseBean;
import com.beans.booking.GetAllBookingsResponseBean;
import com.beans.booking.GetBookingForQuickSearchResponseBean;
import com.beans.booking.GetBranchAvailableModelsForBookingComboboxItemsRequestBean;
import com.beans.contract.*;
import com.beans.customer.GetCountriesPhoneResponseBean;
import com.beans.customer.SearchCustomerRequestBean;
import com.beans.driver.*;
import com.beans.general.AbpResponseBean;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.lookups.GetItemsByTypeResponseBean;
import com.beans.validation.IsValidPhoneResponseBean;
import com.beans.vehicle.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.CreateBookingResponseWrapper;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for orchestrating execute booking operations.
 * This service coordinates multiple API calls to execute a booking flow.
 */
@Service
public class ExecuteBookingOperations {

    @Autowired
    private BookingOperationsService bookingOperationsService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ExecuteBookingService executeBookingService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private LookupsService lookupsService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Execute created booking with new customer and new vehicle.
     * This method orchestrates a series of API calls to execute a booking that was previously created.
     *
     * @param countryName The country name.
     * @param branchName The branch name.
     * @return The response from executing the booking.
     */
    @LogExecutionTime
    public AbpResponseBean ExecuteCreatedBookingWithNewCustomerAndNewVehicle(String countryName, String branchName) {
        // Step 1: Create booking with new customer and new vehicle
        CreateBookingResponseWrapper createBookingResponseWrapper = bookingOperationsService.CreateBookingWithNewCustomerAndNewVehicle(countryName, branchName);
        String bookingNumber = createBookingResponseWrapper.getCreateBookingResponseBean().result().bookingNumber();
        Long bookingId = Long.valueOf(createBookingResponseWrapper.getCreateBookingResponseBean().result().bookingId());

        // Step 2: Get all bookings using the booking number
        String getAllBookingsRequest = "page=1&pageSize=15&filter=bookingNumber~eq~'" + bookingNumber + "'&sort=pickupDate-";
        GetAllBookingsResponseBean getAllBookingsResponse = bookingService.getAllBookings(getAllBookingsRequest);
        
        // Step 3: Get booking for quick search using booking number
        GetBookingForQuickSearchResponseBean getBookingForQuickSearchResponse = dashboardService.getBookingForQuickSearch(bookingNumber);

        // Extract booking details from GetBookingForQuickSearch response
        GetBookingForQuickSearchResponseBean.BookingQuickSearchResult bookingQuickSearchResult =  getBookingForQuickSearchResponse != null && getBookingForQuickSearchResponse.result() != null ?   getBookingForQuickSearchResponse.result() : null;
        Integer pickupBranchId = bookingQuickSearchResult != null ? bookingQuickSearchResult.pickupBranchId() : null;
        Integer bookingTypeId = bookingQuickSearchResult != null ? bookingQuickSearchResult.bookingTypeId() : null;
        String pickupDateFromBooking = bookingQuickSearchResult != null ? bookingQuickSearchResult.pickupDate() : null;
        Integer vehicleIdFromBooking = bookingQuickSearchResult != null ? bookingQuickSearchResult.vehicleId() : null;
        AbpResponseBean isAllowedToExecuteResponse = executeBookingService.isAllowedToExecuteBooking(bookingId);
        Long customerId =  getAllBookingsResponse.result().data().get(0).driverId();
        executeBookingService.getLiteCustomer(customerId);
        IsValidPhoneResponseBean isValidPhone = bookingService.isValidPhone(getAllBookingsResponse.result().data().get(0).customerPrimaryPhone().split("-")[1], getAllBookingsResponse.result().data().getFirst().customerPrimaryPhone().split("-")[0]);
        settingsService.getTenantSettingBySettingKey("App.TenantManagement.EnablePageTracking");
        GetAllItemsComboboxItemsResponseBean contractStates = lookupsService.getAllItemsComboboxItems(26, false, true);
        GetItemsByTypeResponseBean privacyPolicyTypes =lookupsService.getItemsByType(266, false);
        GetCountriesPhoneResponseBean countriesPhone= contractService.getCountriesPhone();
        GetAllCarModelsResponseBean vehicleModels= vehicleService.getAllCarModels();
        GetAllItemsComboboxItemsResponseBean fuelLevels =lookupsService.getAllItemsComboboxItems(12, false, false);
        GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApiResponseBean externalLoyaltiesConfigurationsItems =contractService.getExternalLoyaltiesConfigurationsItemsFromLoyaltyApi(false);

        String dropoffDate = null;
        Integer modeId = 32200;

        GetReadyVehiclesModelResponseBean getReadyVehiclesModelResponse = executeBookingService.getReadyVehiclesModel(pickupBranchId, , modeId);
        GetReadyVehiclesByCategoryAndModelRequestBean getReadyVehiclesRequest = new GetReadyVehiclesByCategoryAndModelRequestBean(vehicleCategoryId, vehicleModelId, pickupBranchId, false, false);
        GetReadyVehiclesByCategoryAndModelResponseBean getReadyVehiclesResponse = executeBookingService.getReadyVehiclesByCategoryAndModel(getReadyVehiclesRequest);
//        Integer vehicleIdFromBooking = getReadyVehiclesResponse.result().items().get(0).id();
        Integer      vehicleModelId = getReadyVehiclesResponse.result().items().get(0).modelId();
        Integer      vehicleCategoryId = getReadyVehiclesResponse.result().items().get(0).categoryId();
        Integer      vehicleYear = getReadyVehiclesResponse.result().items().get(0).year();
        // Step 17: Validate contract info
        if (vehicleIdFromBooking != null && pickupBranchId != null && customerId != null && pickupDateFromBooking != null) {
            // Need dropoffDate - extract from booking or calculate
            if (dropoffDate == null && pickupDateFromBooking != null) {
                // Calculate dropoffDate (typically 1 day after pickup for booking)
                dropoffDate = pickupDateFromBooking; // This should be calculated properly
            }
            
            ValidateContractInfoRequestBean.CustomerInfo customerInfo = new ValidateContractInfoRequestBean.CustomerInfo(
                customerId, null); // identityId should be extracted from customer response
            ValidateContractInfoRequestBean validateContractInfoRequest = new ValidateContractInfoRequestBean(
                vehicleIdFromBooking, pickupBranchId, pickupBranchId, pickupDateFromBooking, dropoffDate, 
                String.valueOf(bookingId), customerInfo, true);
            contractService.validateContractInfo(validateContractInfoRequest);
        }

        // Step 18: Validate prevent renting restriction
        if (customerId != null && pickupBranchId != null && vehicleModelId != null && pickupDateFromBooking != null) {
            contractService.validatePreventRentingRestriction(customerId, pickupBranchId, vehicleModelId, pickupDateFromBooking);
        }

        // Step 19: Get external loyalties with allow earn combobox
        if (customerId != null && pickupBranchId != null) {
            contractService.getExternalLoyaltiesWithAllowEarnComboboxFromLoyaltyApi(customerId, pickupBranchId);
        }

        // Step 20: Validate customer
        if (customerId != null) {
            executeBookingService.validateCustomer(customerId);
        }

        // Step 21: Check if customer is eligible for customer providers integration
        if (customerId != null) {
            executeBookingService.isCustomerEligibleForCustomerProvidersIntegration(customerId);
        }

        // Step 22: Get matching offers
        if (customerId != null && vehicleIdFromBooking != null && pickupBranchId != null && 
            vehicleModelId != null && vehicleYear != null && pickupDateFromBooking != null && dropoffDate != null) {
            contractService.getMatchingOffers(customerId, vehicleIdFromBooking, pickupBranchId, 
                pickupDateFromBooking, dropoffDate, vehicleModelId, vehicleYear, false, false, null, false);
        }

        // Step 23: Get contract extra items
        if (pickupBranchId != null && vehicleCategoryId != null) {
            // rentalRatesSchemaPeriodId should be extracted from booking or use default (2)
            contractService.getContractExtraItems(pickupBranchId, vehicleCategoryId, 2, 1800, 230, 120, false, null);
        }

        // Step 24: Get all applicable driver authorization combobox items
        Integer contractMode = 240; // Default contract mode
        GetAllApplicableDriverAuthorizationComboboxItemsResponseBean driverAuthItemsResponse = null;
        if (vehicleIdFromBooking != null && customerId != null) {
            driverAuthItemsResponse = contractService.getAllApplicableDriverAuthorizationComboboxItems(
                vehicleIdFromBooking, customerId, contractMode);
        }

        // Step 25: Cancel driver authorization if cancellation required
        CancelDriverAuthorizationIfCancellationRequiredRequestBean cancelDriverAuthRequest = null;
        if (vehicleIdFromBooking != null && customerId != null && pickupBranchId != null && 
            pickupDateFromBooking != null && dropoffDate != null && driverAuthItemsResponse != null) {
            // Extract driverAuthorizationTypeId from driverAuthItemsResponse (first selected item)
            Integer driverAuthorizationTypeId = null;
            if (driverAuthItemsResponse.result() != null && !driverAuthItemsResponse.result().isEmpty()) {
                for (GetAllApplicableDriverAuthorizationComboboxItemsResponseBean.ComboboxItem item : driverAuthItemsResponse.result()) {
                    if (item.isSelected() != null && item.isSelected()) {
                        driverAuthorizationTypeId = Integer.valueOf(item.value());
                        break;
                    }
                }
            }
            if (driverAuthorizationTypeId == null && !driverAuthItemsResponse.result().isEmpty()) {
                driverAuthorizationTypeId = Integer.valueOf(driverAuthItemsResponse.result().get(0).value());
            }

            if (driverAuthorizationTypeId != null) {
                cancelDriverAuthRequest = new CancelDriverAuthorizationIfCancellationRequiredRequestBean(
                    String.valueOf(pickupBranchId), contractMode, 1234, driverAuthorizationTypeId, 
                    null, null, String.valueOf(customerId), dropoffDate, false, 1100, 
                    pickupDateFromBooking, String.valueOf(vehicleIdFromBooking));
                contractService.cancelDriverAuthorizationIfCancellationRequired(true, cancelDriverAuthRequest);
            }
        }

        // Step 26: Authorize driver
        AuthorizeDriverRequestBean authorizeDriverRequest = null;
        if (cancelDriverAuthRequest != null && vehicleIdFromBooking != null && customerId != null && 
            pickupBranchId != null && pickupDateFromBooking != null && dropoffDate != null) {
            authorizeDriverRequest = new AuthorizeDriverRequestBean(
                String.valueOf(pickupBranchId), contractMode, 1234, 
                Integer.valueOf(cancelDriverAuthRequest.driverAuthorizationTypeId()), 
                String.valueOf(customerId), dropoffDate, false, "966-515546871", 1100, 
                pickupDateFromBooking, String.valueOf(vehicleIdFromBooking), 1111);
            contractService.authorizeDriver(authorizeDriverRequest);
        }

        // Step 27: Calculate billing information
        CalculateBillingInformationResponseBean calculateBillingResponse = null;
        if (vehicleIdFromBooking != null && vehicleModelId != null && vehicleYear != null && 
            customerId != null && vehicleCategoryId != null && pickupBranchId != null && 
            pickupDateFromBooking != null && dropoffDate != null) {
            // Extract rentalRateId from getReadyVehiclesResponse
            Integer rentalRateId = null;
            if (getReadyVehiclesResponse != null && getReadyVehiclesResponse.result() != null && 
                getReadyVehiclesResponse.result().items() != null && !getReadyVehiclesResponse.result().items().isEmpty()) {
                rentalRateId = getReadyVehiclesResponse.result().items().get(0).rentalRate().rentalRateId();
            }

            CalculateBillingInformationRequestBean calculateBillingRequest = new CalculateBillingInformationRequestBean(
                vehicleIdFromBooking, rentalRateId, pickupDateFromBooking, dropoffDate, 210, 2300, 
                pickupBranchId, pickupBranchId, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                vehicleCategoryId, cancelDriverAuthRequest != null ? Integer.valueOf(cancelDriverAuthRequest.driverAuthorizationTypeId()) : null,
                customerId, vehicleModelId, 230);
            calculateBillingResponse = contractService.calculateBillingInformation(calculateBillingRequest);
        }

        // Step 28: Get vehicle check preparation data
        GetVehicleCheckPreparationDataResponseBean vehicleCheckPrepResponse = null;
        if (vehicleIdFromBooking != null) {
            vehicleCheckPrepResponse = vehicleService.getVehicleCheckPreparationData(vehicleIdFromBooking, 1, 120);
        }

        // Step 29: Block vehicle usage for long period
        AbpResponseBean blockVehicleResponse = null;
        if (vehicleIdFromBooking != null && pickupBranchId != null) {
            blockVehicleResponse = contractService.blockVehicleUsageForLongPeriod(vehicleIdFromBooking, pickupBranchId);
        }
        
        // Extract readyVehicleBlockingKey from blockVehicleResponse
        String readyVehicleBlockingKey = null;
        if (blockVehicleResponse != null && blockVehicleResponse.result() != null) {
            readyVehicleBlockingKey = blockVehicleResponse.result().toString();
        }

        // Step 30: Execute booking
        if (calculateBillingResponse != null && vehicleIdFromBooking != null && customerId != null && 
            pickupBranchId != null && pickupDateFromBooking != null && dropoffDate != null &&
            readyVehicleBlockingKey != null && bookingId != null) {
            
            // Build ExecuteBookingRequestBean from all collected data
            ExecuteBookingRequestBean executeBookingRequest = buildExecuteBookingRequest(
                createBookingResponse, getBookingForQuickSearchResponse, getAllBookingsResponse,
                getReadyVehiclesResponse, calculateBillingResponse, vehicleCheckPrepResponse,
                vehicleIdFromBooking, customerId, pickupBranchId, pickupDateFromBooking, dropoffDate,
                readyVehicleBlockingKey, bookingId, cancelDriverAuthRequest, authorizeDriverRequest);
            
            return executeBookingService.executeBooking(executeBookingRequest);
        }

        return new AbpResponseBean(null, null, true, null, false, true);
    }

    /**
     * Build ExecuteBookingRequestBean from collected response data.
     */
    private ExecuteBookingRequestBean buildExecuteBookingRequest(
            CreateBookingResponseBean createBookingResponse,
            GetBookingForQuickSearchResponseBean getBookingForQuickSearchResponse,
            GetAllBookingsResponseBean getAllBookingsResponse,
            GetReadyVehiclesByCategoryAndModelResponseBean getReadyVehiclesResponse,
            CalculateBillingInformationResponseBean calculateBillingResponse,
            GetVehicleCheckPreparationDataResponseBean vehicleCheckPrepResponse,
            Integer vehicleId, Long customerId, Integer pickupBranchId, String pickupDate, String dropoffDate,
            String readyVehicleBlockingKey, Long bookingId,
            CancelDriverAuthorizationIfCancellationRequiredRequestBean cancelDriverAuthRequest,
            AuthorizeDriverRequestBean authorizeDriverRequest) {
        
        // Extract data for ExecuteBookingRequestBean
        Integer rentalRateId = null;
        Integer vehicleCategoryId = null;
        Integer vehicleModelId = null;
        Integer vehicleYear = null;
        Integer fuelId = null;
        Integer odometer = null;
        
        if (getReadyVehiclesResponse != null && getReadyVehiclesResponse.result() != null && 
            getReadyVehiclesResponse.result().items() != null && !getReadyVehiclesResponse.result().items().isEmpty()) {
            var firstVehicle = getReadyVehiclesResponse.result().items().get(0);
            rentalRateId = firstVehicle.rentalRate().rentalRateId();
            vehicleCategoryId = firstVehicle.categoryId();
            vehicleModelId = firstVehicle.modelId();
            vehicleYear = firstVehicle.year();
            fuelId = firstVehicle.fuelId();
            odometer = firstVehicle.odometer();
        }

        // Build contract payment info
        ExecuteBookingRequestBean.Money amount = new ExecuteBookingRequestBean.Money(
            calculateBillingResponse != null && calculateBillingResponse.result() != null &&
            calculateBillingResponse.result().paymentInfo() != null ? 
            calculateBillingResponse.result().paymentInfo().remainingAmount() : 0.0, 1, "SAR");
        
        ExecuteBookingRequestBean.BasePaymentInformationDto basePaymentInfo = 
            new ExecuteBookingRequestBean.BasePaymentInformationDto("CashPaymentInformationDto", 290, amount);
        
        ExecuteBookingRequestBean.VoucherCreateInput voucherInput = 
            new ExecuteBookingRequestBean.VoucherCreateInput(null, 2300, 270, basePaymentInfo, pickupDate, 120);
        
        ExecuteBookingRequestBean.ContractPaymentInfo contractPaymentInfo = 
            new ExecuteBookingRequestBean.ContractPaymentInfo(List.of(voucherInput));

        // Build calculation result from calculateBillingResponse
        ExecuteBookingRequestBean.CalculationResult calculationResult = null;
        if (calculateBillingResponse != null && calculateBillingResponse.result() != null) {
            var calcResult = calculateBillingResponse.result().calculationResult();
            List<ExecuteBookingRequestBean.CalculationItem> calculationItems = new ArrayList<>();
            if (calcResult.calculationItems() != null) {
                for (var item : calcResult.calculationItems()) {
                    ExecuteBookingRequestBean.LocalizedString localizedString = 
                        new ExecuteBookingRequestBean.LocalizedString(
                            item.itemNameLocalized().stringValue(), 
                            item.itemNameLocalized().currentCultureText());
                    ExecuteBookingRequestBean.Duration duration = 
                        new ExecuteBookingRequestBean.Duration(item.duration().start(), item.duration().end());
                    ExecuteBookingRequestBean.CalculationItem calcItem = 
                        new ExecuteBookingRequestBean.CalculationItem(
                            item.itemTypeId(), item.itemName(), localizedString, item.itemNameRaw(),
                            item.quantity(), item.isPctDiscount(), item.discountPCT(), item.discountAmount(),
                            item.minimumDiscount(), item.maximumDiscount(), item.minimumDiscountAmount(), 
                            item.maximumDiscountAmount(), item.discountDescription(), item.taxPercentage(),
                            item.taxAmount(), item.unitPrice(), item.totalPrice(), item.netPrice(), 
                            item.isVisible(), item.unitDescriptionId(), item.unitDescription(), duration,
                            item.taxConfigurations(), item.currencyFraction(), item.discountCapAmount(),
                            item.originalDiscountPct(), item.isSecretRate());
                    calculationItems.add(calcItem);
                }
            }
            calculationResult = new ExecuteBookingRequestBean.CalculationResult(
                calcResult.netAmount(), calcResult.itemsSummary(), calcResult.insuranceDeposit(),
                calcResult.isAdditional(), calcResult.isSecretRate(), calcResult.isCummlative(), calculationItems);
        }

        // Build vehicle info
        ExecuteBookingRequestBean.VehicleInfo vehicleInfo = 
            new ExecuteBookingRequestBean.VehicleInfo(vehicleId, vehicleCategoryId, odometer, fuelId, pickupBranchId);

        // Build customer info - need identityId from customer response
        Long identityId = null; // Should be extracted from getLiteCustomer or searchCustomer response
        ExecuteBookingRequestBean.CustomerInfo customerInfo = 
            new ExecuteBookingRequestBean.CustomerInfo(customerId, identityId);

        // Build vehicle check data from vehicleCheckPrepResponse
        ExecuteBookingRequestBean.VehicleCheckData vehicleCheckData = null;
        if (vehicleCheckPrepResponse != null && vehicleCheckPrepResponse.result() != null) {
            var prepResult = vehicleCheckPrepResponse.result();
            ExecuteBookingRequestBean.Signature signature = 
                new ExecuteBookingRequestBean.Signature(null); // Should be populated from actual check
            
            ExecuteBookingRequestBean.ReferenceDetails referenceDetails = 
                new ExecuteBookingRequestBean.ReferenceDetails(prepResult.checkType() != null ? prepResult.checkType().id() : 1);
            
            ExecuteBookingRequestBean.SkeletonImage skeletonImage = null;
            if (prepResult.vehicleSkeletonDetails() != null && prepResult.vehicleSkeletonDetails().image() != null) {
                var img = prepResult.vehicleSkeletonDetails().image();
                skeletonImage = new ExecuteBookingRequestBean.SkeletonImage(
                    img.id(), img.url(), img.isNewDocument());
            }
            
            ExecuteBookingRequestBean.SkeletonDetails skeletonDetails = 
                new ExecuteBookingRequestBean.SkeletonDetails(
                    prepResult.vehicleSkeletonDetails() != null ? prepResult.vehicleSkeletonDetails().id() : 1,
                    skeletonImage);
            
            // Build check item statuses
            List<ExecuteBookingRequestBean.CheckItemStatus> checkItemStatuses = new ArrayList<>();
            if (prepResult.checklistDetails() != null) {
                for (var checklist : prepResult.checklistDetails()) {
                    if (checklist.checkItems() != null) {
                        for (var checkItem : checklist.checkItems()) {
                            // Use default choiceId (5000 for OK)
                            checkItemStatuses.add(new ExecuteBookingRequestBean.CheckItemStatus(
                                checklist.id(), checkItem.id(), 5000));
                        }
                    }
                }
            }
            
            ExecuteBookingRequestBean.VehicleCheckDamages vehicleCheckDamages = 
                new ExecuteBookingRequestBean.VehicleCheckDamages(
                    checkItemStatuses, new ArrayList<>(), new ArrayList<>());
            
            ExecuteBookingRequestBean.Money totalDamagesCost = 
                new ExecuteBookingRequestBean.Money(0.0, 1, "SAR");
            
            vehicleCheckData = new ExecuteBookingRequestBean.VehicleCheckData(
                vehicleId, fuelId, odometer, signature, referenceDetails, skeletonDetails,
                vehicleCheckDamages, new ArrayList<>(), totalDamagesCost, null, null);
        }

        // Determine driverAuthorizationTypeId
        Integer driverAuthorizationTypeId = null;
        if (cancelDriverAuthRequest != null) {
            driverAuthorizationTypeId = Integer.valueOf(cancelDriverAuthRequest.driverAuthorizationTypeId());
        }

        // Build final ExecuteBookingRequestBean
        return new ExecuteBookingRequestBean(
            rentalRateId, contractPaymentInfo, new ArrayList<>(), null, new ArrayList<>(),
            String.valueOf(bookingId), calculationResult, pickupBranchId, pickupBranchId, dropoffDate,
            120, null, new ArrayList<>(), vehicleInfo, customerInfo, true, readyVehicleBlockingKey,
            pickupDate, new HashMap<>(), "", driverAuthorizationTypeId, vehicleCheckData);
    }


}
