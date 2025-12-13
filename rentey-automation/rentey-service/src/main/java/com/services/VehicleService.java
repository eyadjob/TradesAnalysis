package com.services;

import com.annotation.LogExecutionTime;
import com.beans.general.AbpResponseBean;
import com.beans.general.GetAllItemsComboboxItemsResponseBean;
import com.beans.general.UploadBase64FileRequestBean;
import com.beans.general.UploadBase64FileResponseBean;
import com.beans.vehicle.*;
import com.util.EncodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service for interacting with vehicle-related APIs.
 */
@Service
public class VehicleService {

    @Autowired
    @Qualifier("settingsWebClient")
    private WebClient settingsWebClient;

    @Autowired
    @Qualifier("apiBasePath")
    private String apiBasePath;

    @Qualifier("apiBasePathWithoutService")
    private String apiBasePathWithoutService;


    /**
     * Get insurance company combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the insurance companies (required).
     * @param includeInActive Whether to include inactive insurance companies (default: false).
     * @return The response containing all insurance company combobox items.
     */
    @Cacheable(cacheNames = "allInsuranceCompaniesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(
            Integer countryId, Boolean includeInActive) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/InsuranceCompany/GetInsuranceCompanyComboboxItems");

                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    @Cacheable(cacheNames = "allInsuranceCompaniesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getInsuranceCompanyComboboxItems(int countryId) {
        return getInsuranceCompanyComboboxItems(countryId, false);
    }

    public String getInsuranceCompanyIdByName(GetAllItemsComboboxItemsResponseBean insuranceResponseBean, String insuranceCompanyName) {
        return insuranceResponseBean.result().items().stream().filter(insuranceCompany -> insuranceCompany.displayText().equals(insuranceCompanyName))
                .findFirst().map(insuranceCompany -> String.valueOf(insuranceCompany.value())).orElse("-1");
    }

    /**
     * Get all accident policies.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the accident policies (required).
     * @param includeInactive Whether to include inactive policies (default: false).
     * @param request         The request query string containing pagination, filter, and sort parameters (optional).
     *                        Example: "page=1&pageSize=15&filter=(isActive~eq~true~and~isExpired~eq~false)&sort=lastUpdateTime-desc"
     * @return The response containing all accident policies.
     */
    @Cacheable(cacheNames = "allAccidentPoliciesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllAccidentPoliciesResponseBean getAllAccidentPolicies(
            Integer countryId,
            Boolean includeInactive,
            String request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/AccidentPolicy/GetAllAccidentPolicies");

                    if (countryId != null) {
                        builder.queryParam("CountryId", countryId);
                    }
                    if (includeInactive != null) {
                        builder.queryParam("IncludeInactive", includeInactive);
                    }
                    if (request != null && !request.isEmpty()) {
                        // Decode the request parameter if it's already URL-encoded to prevent double encoding
                        String decodedRequest = EncodingUtil.decodeIfEncoded(request);
                        builder.queryParam("Request", decodedRequest);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllAccidentPoliciesResponseBean.class)
                .block();
    }

    /**
     * Get all accident policies.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the accident policies (required).
     * @return The response containing all accident policies.
     */
    @Cacheable(cacheNames = "allAccidentPoliciesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllAccidentPoliciesResponseBean getAllAccidentPolicies(
            Integer countryId
    ) {
        return getAllAccidentPolicies(countryId, false, "page%3D1%26pageSize%3D15%26filter%3D(isActive~eq~true~and~isExpired~eq~false)%26sort%3DlastUpdateTime-desc");
    }

    public String getAccidentPolicyNumberByOrganizationName(GetAllAccidentPoliciesResponseBean accidentPoliciesResponseBean, String accidentPolicyInsuranceName) {
        return accidentPoliciesResponseBean.result().data().stream().filter(accidentPolicy -> accidentPolicy.insuranceCompany().equals(accidentPolicyInsuranceName))
                .findFirst().map(accidentPolicy -> String.valueOf(accidentPolicy.id())).orElse("-1");
    }

    /**
     * Get all car models.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all car models.
     */
    @Cacheable(cacheNames = "allCarsModelsCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllCarModelsResponseBean getAllCarModels() {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(apiBasePath + "/CarModel/GetAllCarModels")
                .retrieve()
                .bodyToMono(GetAllCarModelsResponseBean.class)
                .block();
    }

    /**
     * Get car model ID by name.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param carModelsResponseBean The response containing all car models.
     * @param carModelName          The name of the car model to find.
     * @return The ID of the car model, or "-1" if not found.
     */
    public String getCarModelIdByName(GetAllCarModelsResponseBean carModelsResponseBean, String carModelName) {
        return carModelsResponseBean.result().items().stream()
                .filter(carModel -> carModel.name() != null && carModel.name().equals(carModelName))
                .findFirst()
                .map(carModel -> String.valueOf(carModel.id()))
                .orElse("-1");
    }

    /**
     * Get fuel types for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId       The country ID for which to get the fuel types (required).
     * @param includeInActive Whether to include inactive fuel types (default: false).
     * @param selectedId      The selected fuel type ID (default: -1).
     * @return The response containing all fuel types for combobox.
     */
    @Cacheable(cacheNames = "fuelTypesForCombobox", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            Integer countryId, Boolean includeInActive,
            Integer selectedId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/FuelType/GetFuelTypesForCombobox");

                    if (includeInActive != null) {
                        builder.queryParam("includeInActive", includeInActive);
                    }
                    if (countryId != null) {
                        builder.queryParam("countryId", countryId);
                    }
                    if (selectedId != null) {
                        builder.queryParam("selectedId", selectedId);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetAllItemsComboboxItemsResponseBean.class)
                .block();
    }

    /**
     * Get fuel types for combobox.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The country ID for which to get the fuel types (required).
     * @return The response containing all fuel types for combobox.
     */
    @Cacheable(cacheNames = "fuelTypesForCombobox", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllItemsComboboxItemsResponseBean getFuelTypesForCombobox(
            Integer countryId
    ) {
        return getFuelTypesForCombobox(countryId, false, -1);
    }

    /**
     * Get vendor combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param includeInactive Whether to include inactive vendors (default: false).
     * @return The response containing all vendor combobox items.
     */

    @Cacheable(cacheNames = "vendorComboboxCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetVendorComboboxItemsResponseBean getVendorComboboxItems(Boolean includeInactive) {
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/Vendor/GetVendorComboboxItems");

                    if (includeInactive != null) {
                        builder.queryParam("IncludeInactive", includeInactive);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetVendorComboboxItemsResponseBean.class)
                .block();
    }


    /**
     * Get vendor combobox items.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The response containing all vendor combobox items.
     */
    @Cacheable(cacheNames = "vendorComboboxCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetVendorComboboxItemsResponseBean getVendorComboboxItems() {
        return getVendorComboboxItems(false);
    }

    @LogExecutionTime
    public String getVendorIdByName(GetVendorComboboxItemsResponseBean vendorComboboxItemsResponseBean, String vendorName) {
        return vendorComboboxItemsResponseBean.result().items().stream()
                .filter(vendorComboboxItem -> vendorComboboxItem.displayText().equals(vendorName))
                .findFirst()
                .map(vehicleVendor -> String.valueOf(vehicleVendor.value()))
                .orElse("-1");
    }

    /**
     * Retrieves a paginated list of vehicles for a specific branch, filtered by country and plate number.
     * The results are sorted by last modification time in descending order.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param countryId The ID of the country to filter vehicles (required, non-null)
     * @param branchId The ID of the branch to get vehicles from
     * @param vehiclePlateNumber The plate number or part of it to search for (case-sensitive)
     * @return GetAllBranchVehiclesResponseBean containing the paginated list of matching vehicles,
     *         or null if no vehicles are found
     * @throws org.springframework.web.reactive.function.client.WebClientResponseException if the request fails
     * @see com.beans.vehicle.GetAllBranchVehiclesResponseBean
     */
    @Cacheable(cacheNames = "allBranchVehiclesCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetAllBranchVehiclesResponseBean getAllBranchVehicles( int countryId, int branchId,@NonNull  String vehiclePlateNumber) {
        String plateNumberWithEncodedSpaces = vehiclePlateNumber.replace(" ", "%20");
        String filter = String.format("(countryId~eq~%d~and~currentLocationId~eq~%d~and~plateNo~contains~'%s')",
                countryId, branchId, plateNumberWithEncodedSpaces).replace("'","%27");
//        ~and~statusId~eq~140
        String request = String.format("page=1&pageSize=15&sort=lastModificationTime-desc&filter=%s", filter);
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(apiBasePath + "/RentalVehicle/GetAllBranchVehicles");
                    uriBuilder.queryParam("Request", request);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(GetAllBranchVehiclesResponseBean.class)
                .block();
    }


    /**
     * Get vehicle check preparation data.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param vehicleId The vehicle ID (required).
     * @param checkTypeId The check type ID (required).
     * @param sourceId The source ID (required).
     * @return The response containing vehicle check preparation data.
     */
    @Cacheable(cacheNames = "vehicleCheckPreparationDataCache", keyGenerator = "AutoKeyGenerator")
    @LogExecutionTime
    public GetVehicleCheckPreparationDataResponseBean getVehicleCheckPreparationData(
            Integer vehicleId, Integer checkTypeId, Integer sourceId) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(apiBasePath + "/VehicleCheck/GetVehicleCheckPreparationData");

                    if (vehicleId != null) {
                        builder.queryParam("VehicleId", vehicleId);
                    }
                    if (checkTypeId != null) {
                        builder.queryParam("CheckTypeId", checkTypeId);
                    }
                    if (sourceId != null) {
                        builder.queryParam("SourceId", sourceId);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(GetVehicleCheckPreparationDataResponseBean.class)
                .block();
    }

    /**
     * Upload base64 file.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing base64 encoded file data (e.g., "data:image/jpeg;base64,...").
     * @return The response containing the uploaded file information.
     */
    @LogExecutionTime
    @Cacheable(cacheNames = "uploadBase64FileCachedData", keyGenerator = "AutoKeyGenerator")
    public UploadBase64FileResponseBean uploadBase64File(UploadBase64FileRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePathWithoutService + "/FileUpload/UploadBase64File")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UploadBase64FileResponseBean.class)
                .block();
    }

    /**
     * Upload signature image as base64 and return the virtual path.
     * This method uploads a default signature image and returns its virtual path.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @return The virtual path of the uploaded signature image, or null if upload fails.
     */
    @LogExecutionTime
    public String uploadSignatureImage() {
        String base64ImageData = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAD6APoDAREAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFAEBAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AJ/4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP/9k=";
        UploadBase64FileRequestBean uploadRequest = new UploadBase64FileRequestBean(base64ImageData);
        UploadBase64FileResponseBean uploadResponse = uploadBase64File(uploadRequest);
        
        if (uploadResponse != null && uploadResponse.result() != null && uploadResponse.result().virtualPath() != null) {
            return uploadResponse.result().virtualPath();
        }
        return null;
    }

    /**
     * Receive new vehicle.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing vehicle check information for receiving a new vehicle.
     * @return The response containing the result of the operation.
     */
    @LogExecutionTime
    public AbpResponseBean receiveNewVehicle(ReceiveNewVehicleRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/RentalVehicle/ReceiveNewVehicle")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AbpResponseBean.class)
                .block();
    }

    /**
     * Create vehicles.
     * Authorization header and all headers from RenteyConfiguration are automatically included.
     *
     * @param request The request containing vehicle information to create.
     * @return The response containing the result of the operation.
     */
    @LogExecutionTime
    public CreateVehiclesResponseBean createVehicles(CreateVehiclesRequestBean request) {
        // Authorization header and all headers from RenteyConfiguration are automatically included
        return settingsWebClient.post()
                .uri(apiBasePath + "/Vehicle/CreateVehicles")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateVehiclesResponseBean.class)
                .block();
    }

}
