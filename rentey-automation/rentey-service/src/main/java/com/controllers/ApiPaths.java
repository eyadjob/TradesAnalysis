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
    
    // Currency endpoints
    public static final String CURRENCY_GET_COUNTRY_CURRENCY_INFO = "/Currency/GetCountryCurrencyInfo";
    public static final String CURRENCY_GET_CURRENCIES_FOR_COMBOBOX = "/Currency/GetCurrenciesForCombobox";
    
    // Branch endpoints
    public static final String BRANCH_GET_USER_BRANCHES_FOR_COMBOBOX = "/Branch/GetUserBranchesForCombobox";
    
    // Customer endpoints
    public static final String CUSTOMER_CREATE_OR_UPDATE = "/Customer/CreateOrUpdateCustomer";
    public static final String CUSTOMER_GET_ALL_ITEMS_COMBOBOX = "/Customer/GetAllItemsComboboxItems";

    //Import Customer endpoints
    public static final String IMPORT_CUSTOMER_FROM_CSV_FILE = "/import-customer-from-csv-file";

    // File Upload endpoints
    public static final String FILE_UPLOAD_BASE64 = "/FileUpload/UploadBase64File";
    
    // Lookups endpoints
    public static final String LOOKUPS_GET_ALL_ITEMS_COMBOBOX = "/Lookups/GetAllItemsComboboxItems";
    public static final String LOOKUPS_GET_TYPES_COMBOBOX = "/Lookups/GetTypesComboboxItems";
    
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
    
    // Private constructor to prevent instantiation
    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

