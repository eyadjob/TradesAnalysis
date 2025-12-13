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

    public static final String BASE_PATH_WITHOUT_SERVICE = "/api";

    // Permission endpoints
    public static final String PERMISSION_GET_ALL = "/Permission/GetAllPermissions";
    public static final String ROLE_CREATE_OR_UPDATE = "/Role/CreateOrUpdateRole";
    
    // Settings endpoints
    public static final String TENANT_SETTINGS_UPDATE_ALL = "/TenantSettings/UpdateAllSettings";
    public static final String GEO_SETTINGS_CHANGE_TENANT = "/GeoSettings/ChangeTenantSettings";
    public static final String GEO_SETTINGS_UPDATE_COUNTRY = "/GeoSettings/UpdateCountrySettings";
    public static final String GEO_SETTINGS_CHANGE_BRANCH = "/GeoSettings/ChangeBranchSettings";
    
    // Country endpoints
    public static final String COUNTRY_GET_OPERATIONAL_COUNTRIES = "/Country/GetOperationalCountries";
    public static final String COUNTRY_GET_COUNTRIES_PHONE = "/Country/GetCountriesPhone";
    public static final String COUNTRY_GET_COUNTRIES_FOR_COMBOBOX = "/Country/GetCountriesForCombobox";
    public static final String COUNTRY_GET_NATIONALITIES_FOR_COMBOBOX = "/Country/GetNationalitiesForCombobox";
    
    // Currency endpoints
    public static final String CURRENCY_GET_COUNTRY_CURRENCY_INFO = "/Currency/GetCountryCurrencyInfo";
    public static final String CURRENCY_GET_CURRENCIES_FOR_COMBOBOX = "/Currency/GetCurrenciesForCombobox";
    
    // Branch endpoints
    public static final String BRANCH_GET_USER_BRANCHES_FOR_COMBOBOX = "/Branch/GetUserBranchesForCombobox";
    public static final String BRANCH_GET_BRANCHES_COUNTRIES_COMBOBOX_ITEMS = "/Branch/GetBranchesCountriesComboboxItems";
    
    // Contract endpoints
    public static final String CONTRACT_EXTRA_CONFIGURATION_GET_EXTRAS_NAMES_EXCLUDED = "/ContractExtraConfiguration/GetExtrasNamesExcludedFromBookingPaymentDetails";
    public static final String CONTRACT_EXTRA_CONFIGURATION_GET_CONTRACT_EXTRA_ITEMS = "/ContractExtraConfiguration/GetContractExtraItems";
    
    // Customer endpoints
    public static final String CUSTOMER_CREATE_OR_UPDATE = "/Customer/CreateOrUpdateCustomer";
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
    
    // Insurance Company endpoints
    public static final String INSURANCE_COMPANY_GET_COMBOBOX_ITEMS = "/InsuranceCompany/GetInsuranceCompanyComboboxItems";
    
    // Accident Policy endpoints
    public static final String ACCIDENT_POLICY_GET_ALL = "/AccidentPolicy/GetAllAccidentPolicies";
    
    // Car Model endpoints
    public static final String CAR_MODEL_GET_ALL = "/CarModel/GetAllCarModels";
    
    // Fuel Type endpoints
    public static final String FUEL_TYPE_GET_FOR_COMBOBOX = "/FuelType/GetFuelTypesForCombobox";
    
    // Vendor endpoints
    public static final String VENDOR_GET_COMBOBOX_ITEMS = "/Vendor/GetVendorComboboxItems";
    
    // Vehicle endpoints
    public static final String VEHICLE_CREATE = "/Vehicle/CreateVehicles";
    public static final String VEHICLE_CREATE_WITH_RANDOM_PLATE = "/Vehicle/CreateVehicleWithRandomPlateNumber";
    public static final String VEHICLE_CREATE_AND_RECEIVE = "/Vehicle/CreateAndRecieveVehicler";
    public static final String RENTAL_VEHICLE_GET_ALL_BRANCH_VEHICLES = "/RentalVehicle/GetAllBranchVehicles";
    public static final String VEHICLE_CHECK_GET_PREPARATION_DATA = "/VehicleCheck/GetVehicleCheckPreparationData";
    public static final String RENTAL_VEHICLE_RECEIVE_NEW_VEHICLE = "/RentalVehicle/ReceiveNewVehicle";
    
    // Booking endpoints
    public static final String BOOKING_GET_CREATE_BOOKING_DATE_INPUTS = "/Booking/GetCreateBookingDateInputs";
    
    // Validation endpoints
    public static final String VALIDATE_PHONE_IS_VALID = "/ValidatePhone/IsValid";
    
    // External Loyalty Configuration endpoints
    public static final String EXTERNAL_LOYALTY_CONFIGURATION_GET_ALL_ITEMS = "/ExternalLoyaltyConfiguration/GetAllExternalLoyaltiesConfigurationsItems";
    public static final String EXTERNAL_LOYALTY_CONFIGURATION_GET_INTEGRATED_LOYALTIES = "/ExternalLoyaltyConfiguration/GetIntegratedLoyalties";
    
    // Customer Membership endpoints
    public static final String CUSTOMER_MEMBERSHIP_GET_EXTERNAL_LOYALTIES_WITH_ALLOW_REDEEM_COMBOBOX = "/CustomerMembership/GetExternalLoyaltiesWithAllowRedeemCombobox";
    
    // Private constructor to prevent instantiation
    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

