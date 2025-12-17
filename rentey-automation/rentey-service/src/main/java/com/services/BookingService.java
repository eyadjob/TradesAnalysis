package com.services;

import com.annotation.LogExecutionTime;
import com.beans.booking.CalculateBillingInformationRequestBean;
import com.beans.booking.CalculateBillingInformationResponseBean;
import com.beans.booking.CreateBookingRequestBean;
import com.beans.booking.CreateBookingResponseBean;
import com.beans.booking.GetBestRentalRateForModelResponseBean;
import com.beans.booking.GetCreateBookingDateInputsResponseBean;
import com.beans.booking.ValidateDurationAndLocationsRequestBean;
import com.beans.booking.ValidateDurationAndLocationsResponseBean;
import com.beans.contract.GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean;
import com.beans.country.GetCountrySettingsResponseBean;
import com.beans.customer.GetCountriesPhoneResponseBean;
import com.beans.customer.GetCustomerContractInformationByNameResponseBean;
import com.beans.lookups.GetItemsByTypeResponseBean;
import com.beans.loyalty.GetAllExternalLoyaltiesConfigurationsItemsResponseBean;
import com.beans.loyalty.GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean;
import com.beans.loyalty.GetIntegratedLoyaltiesResponseBean;
import com.beans.validation.IsValidPhoneResponseBean;
import com.enums.LookupTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for interacting with booking-related APIs.
 */
@Service
public class BookingService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @Autowired
    @Qualifier("apiBasePathWithoutService")
    private String apiBasePathWithoutService;

    @Autowired
    CountryService countryService;

    @Autowired
    LookupsService lookupsService;

    @Autowired
    CustomerService customerService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    VehicleOperationsService vehicleOperationsService;

    @Autowired
    ContractService contractService;



    /**
     * Get create booking date inputs.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the booking date inputs (required).
     * @return The response containing create booking date inputs.
     */
    @Cacheable(cacheNames = "createBookingDateInputsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCreateBookingDateInputsResponseBean getCreateBookingDateInputs(Integer countryId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/GetCreateBookingDateInputs")
                        .queryParam("countryId", countryId)
                        .build())
                .retrieve()
                .bodyToMono(GetCreateBookingDateInputsResponseBean.class)
                .block();
    }

    /**
     * Validate phone number.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param phoneNumber The phone number to validate (required).
     * @param phoneCode The phone code (country code) to validate (required).
     * @return The response containing the validation result (true if valid, false otherwise).
     */
    @Cacheable(cacheNames = "isValidPhoneCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public IsValidPhoneResponseBean isValidPhone(String phoneNumber, String phoneCode) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePathWithoutService + "/ValidatePhone/IsValid")
                        .queryParam("phoneNumber", phoneNumber)
                        .queryParam("phoneCode", phoneCode)
                        .build())
                .retrieve()
                .bodyToMono(IsValidPhoneResponseBean.class)
                .block();
    }

    /**
     * Get customer contract information by name.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerName The customer name to search for (required).
     * @return The response containing customer contract information matching the name.
     */
    @Cacheable(cacheNames = "customerContractInformationByNameCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetCustomerContractInformationByNameResponseBean getCustomerContractInformationByName(String customerName) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Customer/GetCustomerContractInformationByName")
                        .queryParam("customerName", customerName)
                        .build())
                .retrieve()
                .bodyToMono(GetCustomerContractInformationByNameResponseBean.class)
                .block();
    }

    /**
     * Get all external loyalties configurations items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInActive Whether to include inactive items (default: false).
     * @return The response containing all external loyalties configurations items.
     */
    @Cacheable(cacheNames = "allExternalLoyaltiesConfigurationsItemsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllExternalLoyaltiesConfigurationsItemsResponseBean getAllExternalLoyaltiesConfigurationsItems(Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/ExternalLoyaltyConfiguration/GetAllExternalLoyaltiesConfigurationsItems");
                    
                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllExternalLoyaltiesConfigurationsItemsResponseBean.class)
                .block();
    }

    /**
     * Get integrated loyalties.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all integrated loyalties.
     */
    @Cacheable(cacheNames = "integratedLoyaltiesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetIntegratedLoyaltiesResponseBean getIntegratedLoyalties() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/ExternalLoyaltyConfiguration/GetIntegratedLoyalties")
                .retrieve()
                .bodyToMono(GetIntegratedLoyaltiesResponseBean.class)
                .block();
    }

    /**
     * Get external loyalties with allow redeem combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param customerId The customer ID (required).
     * @param branchId The branch ID (required).
     * @return The response containing external loyalties with allow redeem combobox items.
     */
    @Cacheable(cacheNames = "externalLoyaltiesWithAllowRedeemComboboxCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean getExternalLoyaltiesWithAllowRedeemCombobox(Integer customerId, Integer branchId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/CustomerMembership/GetExternalLoyaltiesWithAllowRedeemCombobox")
                        .queryParam("customerId", customerId)
                        .queryParam("branchId", branchId)
                        .build())
                .retrieve()
                .bodyToMono(GetExternalLoyaltiesWithAllowRedeemComboboxResponseBean.class)
                .block();
    }

    /**
     * Get best rental rate for model.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID (required).
     * @param branchId The branch ID (required).
     * @param modelId The model ID (required).
     * @param year The year (required).
     * @param pickupDate The pickup date (required, format: yyyy-MM-dd HH:mm:ss).
     * @param dropoffDate The dropoff date (required, format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing the best rental rate for the model.
     */
    @Cacheable(cacheNames = "bestRentalRateForModelCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetBestRentalRateForModelResponseBean getBestRentalRateForModel(
            Integer countryId,
            Integer branchId,
            Integer modelId,
            Integer year,
            String pickupDate,
            String dropoffDate) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/RentalVehicle/GetBestRentalRateForModel")
                        .queryParam("CountryId", countryId)
                        .queryParam("BranchId", branchId)
                        .queryParam("ModelId", modelId)
                        .queryParam("Year", year)
                        .queryParam("PickupDate", pickupDate)
                        .queryParam("DropoffDate", dropoffDate)
                        .build())
                .retrieve()
                .bodyToMono(GetBestRentalRateForModelResponseBean.class)
                .block();
    }

    /**
     * Calculate billing information.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking information for billing calculation.
     * @return The response containing calculated billing information.
     */
    @LogExecutionTime
    public CalculateBillingInformationResponseBean calculateBillingInformation(CalculateBillingInformationRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Booking/CalculateBillingInformation")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CalculateBillingInformationResponseBean.class)
                .block();
    }

    /**
     * Create booking.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing booking information to create a booking.
     * @return The response containing the created booking information.
     */
    @LogExecutionTime
    public CreateBookingResponseBean createBooking(CreateBookingRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/CreateBooking/CreateBooking")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateBookingResponseBean.class)
                .block();
    }

    /**
     * Validate duration and locations for booking.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     * This endpoint validates pickup/dropoff dates and locations for a booking.
     *
     * @param request The request containing booking duration and location information.
     * @param pickupDate The pickup date as a query parameter (format: yyyy-MM-dd HH:mm:ss).
     * @param dropOffDate The dropoff date as a query parameter (format: yyyy-MM-dd HH:mm:ss).
     * @return The response containing validation results.
     */
    @LogExecutionTime
    public ValidateDurationAndLocationsResponseBean validateDurationAndLocations(
            ValidateDurationAndLocationsRequestBean request,
            String pickupDate,
            String dropOffDate) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBasePath + "/Booking/ValidateDurationANDLocations")
                        .queryParam("pickupDate", pickupDate)
                        .queryParam("dropOffDate", dropOffDate)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ValidateDurationAndLocationsResponseBean.class)
                .block();
    }



    public CreateBookingRequestBean buildBookingCreationRequest(String countryName, String branchName) {
        String countryId = countryService.getOperationalCountryIdFromName(countryName);
        String branchId = countryService.getBranchIdByName(countryId, branchName);
        GetCountrySettingsResponseBean countrySettingsResponseBean =  countryService.getCountrySettings(Integer.parseInt(countryId),countryService.buildKeysForSettingsToGet("keys=App.CountryManagement.MinimumHoursToBooking&keys=App.CountryManagement.MinimumHoursToBrokerBooking&keys=App.CountryManagement.EnablePaymentOnSystemBooking&keys=App.CountryManagement.MaximumHoursToExecuteImmediateBooking&keys=App.CountryManagement.EnableExternalAuthorizationOnBooking&keys=App.CountryManagement.ContractMinimumHours&keys=App.CountryManagement.MaxDaysWhenAddContract&keys=App.CountryManagement.FreeHours&keys=App.CountryManagement.EnableFuelCost&keys=App.CountryManagement.MaxOdometerChange&keys=App.CountryManagement.MediumMaxAmount&keys=App.CountryManagement.ApplyExternalDriverAuthorizationOn"));
        GetExtrasNamesExcludedFromBookingPaymentDetailsResponseBean extrasNamesExcludedFromBooking = contractService.getExtrasNamesExcludedFromBookingPaymentDetails();
        GetCountriesPhoneResponseBean countriesPhoneResponseBean =  contractService.getCountriesPhone();
        String residencyTypeId = lookupsService.getLookupItemIdByLookupTypeIdAndItemDisplayName(62,"Saudi Citizen");
        String MaritalStatusesId = lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.MARITAL_STATUSES,"Single");
        String genderId = lookupsService.getLookupItemIdByLookupTypeIdAndItemDisplayName(6,"Male");
        String vipLevelId = lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.VIP_LEVELS,"Level One");
        String relativeLevelId = lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.LEVEL,"Father");
        String creditCardTypeId = lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.CREDIT_CARD_TYPES,"Visa");
        String bankTypeId = lookupsService.getLookupItemIdByLookupTypeNameAndItemDisplayName(LookupTypes.BANK_NAMES,"Visa");
        GetItemsByTypeResponseBean privacyPolicyTypes =lookupsService.getItemsByType(266,false);
        GetCreateBookingDateInputsResponseBean createBookingDateInputs= getCreateBookingDateInputs(Integer.parseInt(countryId));

        String pickupDate = createBookingDateInputs.result().minimumPickupDate();
        String dropOffDate = createBookingDateInputs.result().maximumPickupDate();

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

        ValidateDurationAndLocationsResponseBean validateResponse = validateDurationAndLocations(
                validateRequest,
                pickupDate,
                dropOffDate
        );


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




}

