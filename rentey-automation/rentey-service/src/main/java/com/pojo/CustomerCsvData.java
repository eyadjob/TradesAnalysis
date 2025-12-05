package com.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Wrapper object representing customer data read from CSV file.
 * Corresponds to the CSV columns: FirstName, SecondName, FamilyName, Nationality, Gender,
 * BirthDate, PrimaryPhone, DocumentType, DocumentNumber, DocumentExpireDate,
 * DocumentIssueCountry, licenseNo, LicenseIssueCountry, licenseExpiryDate, MemberShip Level
 */
public record CustomerCsvData(
        @JsonProperty("FirstName") String firstName,
        @JsonProperty("SecondName") String secondName,
        @JsonProperty("FamilyName") String familyName,
        @JsonProperty("Nationality") String nationality,
        @JsonProperty("Gender") String gender,
        @JsonProperty("BirthDate") String birthDate,
        @JsonProperty("PrimaryPhone") String primaryPhone,
        @JsonProperty("DocumentType") String documentType,
        @JsonProperty("DocumentNumber") String documentNumber,
        @JsonProperty("DocumentExpireDate") String documentExpireDate,
        @JsonProperty("DocumentIssueCountry") String documentIssueCountry,
        @JsonProperty("licenseNo") String licenseNo,
        @JsonProperty("LicenseIssueCountry") String licenseIssueCountry,
        @JsonProperty("licenseExpiryDate") String licenseExpiryDate,
        @JsonProperty("MemberShip Level") String membershipLevel
) {
    /**
     * Creates a CustomerCsvData from a map of column values.
     * 
     * @param rowData Map where key is column name and value is cell value
     * @return CustomerCsvData object
     */
    public static CustomerCsvData fromMap(Map<String, String> rowData) {
        return new CustomerCsvData(
                getValue(rowData, "FirstName"),
                getValue(rowData, "SecondName"),
                getValue(rowData, "FamilyName"),
                getValue(rowData, "Nationality"),
                getValue(rowData, "Gender"),
                getValue(rowData, "BirthDate"),
                getValue(rowData, "PrimaryPhone"),
                getValue(rowData, "DocumentType"),
                getValue(rowData, "DocumentNumber"),
                getValue(rowData, "DocumentExpireDate"),
                getValue(rowData, "DocumentIssueCountry"),
                getValue(rowData, "licenseNo"),
                getValue(rowData, "LicenseIssueCountry"),
                getValue(rowData, "licenseExpiryDate"),
                getValue(rowData, "MemberShip Level")
        );
    }

    private static String getValue(Map<String, String> rowData, String key) {
        String value = rowData.get(key);
        if (value == null) {
            // Try case-insensitive lookup
            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    return entry.getValue();
                }
            }
        }
        return value != null ? value.trim() : "";
    }
}

