package com.controllers;

/**
 * API path constants for the rentey-service.
 * 
 * Following SOLID principles:
 * - Single Responsibility: Defines all API path constants in one place
 * - DRY: Eliminates duplication of path strings across controllers
 * - Open/Closed: Easy to extend with new paths without modifying existing code
 * - Maintainability: Single source of truth for all API paths
 */
public final class ApiPaths {
    
    /**
     * Base path for all service API endpoints.
     */
    public static final String BASE_PATH = "/api/services/app";

    public static final String EXTERNAL_LOYALTY_BASE_PATH = "/api/services/app";

    public static final String BASE_PATH_WITHOUT_SERVICE = "/api";

    // Permission endpoints
    public static final String PERMISSION_GET_ALL = "/Permission/GetAllPermissions";
    public static final String ROLE_CREATE_OR_UPDATE = "/Role/CreateOrUpdateRole";
    
    // Settings endpoints
    public static final String TENANT_SETTINGS_UPDATE_ALL = "/TenantSettings/UpdateAllSettings";
    public static final String GEO_SETTINGS_CHANGE_TENANT = "/GeoSettings/ChangeTenantSettings";
    public static final String GEO_SETTINGS_UPDATE_COUNTRY = "/GeoSettings/UpdateCountrySettings";
    public static final String GEO_SETTINGS_CHANGE_BRANCH = "/GeoSettings/ChangeBranchSettings";
    public static final String GEO_SETTINGS_GET_TENANT_SETTING_BY_SETTING_KEY = "/GeoSettings/GetTenantSettingBySettingKey";
    public static final String BRANCH_SETTINGS_GET_SETTINGS = "/BranchSettings/GetSettings";
    
    // Rental Rates Schema endpoints
    public static final String RENTAL_RATES_SCHEMA_GET_ALL = "/RentalRatesSchema/GetAllRentalRatesSchemas";

    // Country endpoints
    public static final String COUNTRY_GET_OPERATIONAL_COUNTRIES = "/Country/GetOperationalCountries";
    public static final String COUNTRY_GET_COUNTRIES_PHONE = "/Country/GetCountriesPhone";
    public static final String COUNTRY_GET_COUNTRIES_FOR_COMBOBOX = "/Country/GetCountriesForCombobox";
    public static final String COUNTRY_GET_NATIONALITIES_FOR_COMBOBOX = "/Country/GetNationalitiesForCombobox";
    
    // Country Settings endpoints
    public static final String COUNTRY_SETTINGS_GET_SETTINGS = "/CountrySettings/GetSettings";
    
    // Currency endpoints
    public static final String CURRENCY_GET_COUNTRY_CURRENCY_INFO = "/Currency/GetCountryCurrencyInfo";
    public static final String CURRENCY_GET_CURRENCIES_FOR_COMBOBOX = "/Currency/GetCurrenciesForCombobox";
    
    // Branch endpoints
    public static final String BRANCH_GET_USER_BRANCHES_FOR_COMBOBOX = "/Branch/GetUserBranchesForCombobox";
    public static final String BRANCH_GET_BRANCHES_COUNTRIES_COMBOBOX_ITEMS = "/Branch/GetBranchesCountriesComboboxItems";
    
    // Contract endpoints
    public static final String CONTRACT_EXTRA_CONFIGURATION_GET_EXTRAS_NAMES_EXCLUDED = "/ContractExtraConfiguration/GetExtrasNamesExcludedFromBookingPaymentDetails";
    public static final String CONTRACT_EXTRA_CONFIGURATION_GET_CONTRACT_EXTRA_ITEMS = "/ContractExtraConfiguration/GetContractExtraItems";
    public static final String CONTRACT_GET_OPEN_CONTRACT_DATE_INPUTS = "/Contract/GetOpenContractDateInputs";
    public static final String CONTRACT_GET_MATCHING_OFFERS = "/Contract/GetMatchingOffers";
    public static final String CONTRACT_CALCULATE_BILLING_INFORMATION = "/Contract/CalculateBillingInformation";
    public static final String CONTRACT_EXECUTE_BOOKING = "/Contract/ExecuteBooking";
    public static final String CONTRACT_VALIDATE_CONTRACT_INFO = "/Contract/ValidateContractInfo";
    public static final String CONTRACT_VALIDATE_PREVENT_RENTING_RESTRICTION = "/Contract/ValidatePreventRentingRestriction";
    public static final String CONTRACT_VALIDATE_CUSTOMER = "/Contract/ValidateCustomer";
    public static final String DRIVER_AUTHORIZATION_GET_ALL_APPLICABLE_DRIVER_AUTHORIZATION_COMBOBOX_ITEMS = "/DriverAuthorization/GetAllApplicableDriverAuthorizationComboboxItems";
    public static final String DRIVER_AUTHORIZATION_CANCEL_DRIVER_AUTHORIZATION_IF_CANCELLATION_REQUIRED = "/DriverAuthorization/CancelDriverAuthorizationIfCancellationRequired";
    public static final String DRIVER_AUTHORIZATION_AUTHORIZE_DRIVER = "/DriverAuthorization/AuthorizeDriver";
    
    // Loyalty API endpoints (calls external loyaltyapigw API)
    public static final String LOYALTY_GET_INTEGRATED_LOYALTIES_FROM_LOYALTY_API = "/Loyalty/GetIntegratedLoyaltiesFromLoyaltyApi";
    public static final String LOYALTY_GET_EXTERNAL_LOYALTIES_CONFIGURATIONS_ITEMS_FROM_LOYALTY_API = "/Loyalty/GetExternalLoyaltiesConfigurationsItemsFromLoyaltyApi";
    public static final String LOYALTY_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_REDEEM_COMBOBOX_FROM_LOYALTY_API = "/Loyalty/GetExternalLoyaltiesWithAllowRedeemComboboxFromLoyaltyApi";
    public static final String LOYALTY_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_EARN_COMBOBOX_FROM_LOYALTY_API = "/Loyalty/GetExternalLoyaltiesWithAllowEarnComboboxFromLoyaltyApi";
    
    // Customer endpoints
    public static final String CUSTOMER_CREATE_OR_UPDATE = "/Customer/CreateOrUpdateCustomer";
    public static final String CUSTOMER_CREATE_WITH_RANDOM_NAME = "/Customer/CreateCustomerWithRandomName";
    public static final String CUSTOMER_SEARCH_CUSTOMER = "/Customer/SearchCustomer";
    public static final String CUSTOMER_GET_LITE_CUSTOMER = "/Customer/GetLiteCustomer";
    public static final String CUSTOMER_IS_CUSTOMER_ELIGIBLE_FOR_CUSTOMER_PROVIDERS_INTEGRATION = "/Customer/IsCustomerEligibleForCustomerProvidersIntegration";
    public static final String CUSTOMER_GET_ALL_ITEMS_COMBOBOX = "/Customer/GetAllItemsComboboxItems";
    public static final String CUSTOMER_GET_CONTRACT_INFORMATION_BY_NAME = "/Customer/GetCustomerContractInformationByName";

    //Import Customer endpoints
    public static final String IMPORT_CUSTOMER_FROM_CSV_FILE = "/import-customer-from-csv-file";

    // File Upload endpoints
    public static final String FILE_UPLOAD_BASE64 = "/FileUpload/UploadBase64File";


    // Lookups endpoints
    public static final String LOOKUPS_GET_ALL_ITEMS_COMBOBOX = "/Lookups/GetAllItemsComboboxItems";
    public static final String LOOKUPS_GET_TYPES_COMBOBOX = "/Lookups/GetTypesComboboxItems";
    public static final String LOOKUPS_GET_ITEMS_BY_TYPE = "/Lookups/GetItemsByType";
    
    // Payment endpoints
    public static final String PAYMENT_GET_PAYMENT_METHODS_COMBOBOX_ITEMS = "/Payment/GetPaymentMethodsComboboxItems";
    
    // Insurance Company endpoints
    public static final String INSURANCE_COMPANY_GET_COMBOBOX_ITEMS = "/InsuranceCompany/GetInsuranceCompanyComboboxItems";
    
    // Accident Policy endpoints
    public static final String ACCIDENT_POLICY_GET_ALL = "/AccidentPolicy/GetAllAccidentPolicies";
    
    // Car Model endpoints
    public static final String CAR_MODEL_GET_ALL = "/CarModel/GetAllCarModels";
    public static final String CAR_MODEL_GET_LITE_CAR_MODEL = "/CarModel/GetLiteCarModel";
    
    // Fuel Type endpoints
    public static final String FUEL_TYPE_GET_FOR_COMBOBOX = "/FuelType/GetFuelTypesForCombobox";
    
    // Vendor endpoints
    public static final String VENDOR_GET_COMBOBOX_ITEMS = "/Vendor/GetVendorComboboxItems";
    
    // Vehicle endpoints
    public static final String VEHICLE_CREATE = "/Vehicle/CreateVehicles";
    public static final String VEHICLE_CREATE_WITH_RANDOM_PLATE = "/Vehicle/CreateVehicleWithRandomPlateNumber";
    public static final String VEHICLE_CREATE_AND_RECEIVE = "/Vehicle/CreateAndRecieveVehicler";
    public static final String RENTAL_VEHICLE_GET_ALL_BRANCH_VEHICLES = "/RentalVehicle/GetAllBranchVehicles";
    public static final String RENTAL_VEHICLE_GET_BEST_RENTAL_RATE_FOR_MODEL = "/RentalVehicle/GetBestRentalRateForModel";
    public static final String RENTAL_VEHICLE_GET_BRANCH_AVAILABLE_MODELS_FOR_BOOKING_COMBOBOX_ITEMS = "/RentalVehicle/GetBranchAvailableModelsForBookingComboboxItems";
    public static final String RENTAL_VEHICLE_GET_READY_VEHICLES_MODEL = "/RentalVehicle/GetReadyVehiclesModel";
    public static final String RENTAL_VEHICLE_BLOCK_VEHICLE_USAGE_FOR_LONG_PERIOD = "/RentalVehicle/BlockVehicleUsageForLongPeriod";
    public static final String RENTAL_VEHICLE_GET_READY_VEHICLES_BY_CATEGORY_AND_MODEL = "/RentalVehicle/GetReadyVehiclesByCategoryAndModel";
    public static final String VEHICLE_CHECK_GET_PREPARATION_DATA = "/VehicleCheck/GetVehicleCheckPreparationData";
    public static final String RENTAL_VEHICLE_RECEIVE_NEW_VEHICLE = "/RentalVehicle/ReceiveNewVehicle";
    
    // Booking endpoints
    public static final String BOOKING_GET_CREATE_BOOKING_DATE_INPUTS = "/Booking/GetCreateBookingDateInputs";
    public static final String BOOKING_GET_ALL_BOOKINGS = "/Booking/GetAllBookings";
    public static final String BOOKING_GET_BOOKING_FOR_QUICK_SEARCH = "/Booking/GetBookingForQuickSearch";
    public static final String BOOKING_IS_ALLOWED_TO_EXECUTE_BOOKING = "/Booking/IsAllowedToExecuteBooking";
    public static final String BOOKING_CALCULATE_BILLING_INFORMATION = "/Booking/CalculateBillingInformation";
    public static final String BOOKING_VALIDATE_DURATION_AND_LOCATIONS = "/Booking/ValidateDurationANDLocations";
    public static final String BOOKING_VALIDATE_PREVENT_RENTING_RESTRICTION = "/Booking/ValidatePreventRentingRestriction";
    public static final String BOOKING_CREATE_BOOKING_WITH_NEW_CUSTOMER_AND_NEW_VEHICLE = "/Booking/CreateBookingWithNewCustomerAndNewVehicle";
    
    // Create Booking endpoints
    public static final String CREATE_BOOKING_CREATE_BOOKING = "/CreateBooking/CreateBooking";
    
    // Validation endpoints
    public static final String VALIDATE_PHONE_IS_VALID = "/ValidatePhone/IsValid";
    
    // External Loyalty Configuration endpoints
    public static final String EXTERNAL_LOYALTY_CONFIGURATION_GET_ALL_ITEMS = "/ExternalLoyaltyConfiguration/GetAllExternalLoyaltiesConfigurationsItems";
    public static final String EXTERNAL_LOYALTY_CONFIGURATION_GET_INTEGRATED_LOYALTIES = "/ExternalLoyaltyConfiguration/GetIntegratedLoyalties";
    
    // Customer Membership endpoints
    public static final String CUSTOMER_MEMBERSHIP_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_REDEEM_COMBOBOX = "/CustomerMembership/GetExternalLoyaltiesWithAllowRedeemCombobox";
    
    // Token Auth endpoints
    public static final String TOKEN_AUTH_AUTHENTICATE = "/TokenAuth/Authenticate";
    
    // Private constructor to prevent instantiation
    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

