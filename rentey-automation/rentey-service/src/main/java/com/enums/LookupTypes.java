package com.enums;

/**
 * Enum representing lookup types used in the system.
 * Each enum constant corresponds to a lookup type display text.
 */
public enum LookupTypes {
    NOT_ASSIGNED("Not assigned"),
    CLASSIFICATIONS("Classifications"),
    MANUFACTURES("Manufactures"),
    SECTORS("Sectors"),
    MARITAL_STATUSES("Marital statuses"),
    LEVEL("Level"),
    COLORS("Colors"),
    TRIM_LEVELS("Trim Levels"),
    DRIVER_LICENSE_CATEGORY("Driver License category"),
    CREDIT_CARD_TYPES("Credit Card Types"),
    BOOKING_CANCEL_REASONS("Booking Cancel Reasons"),
    RELATIONSHIP_TYPES("Relationship Types"),
    VIP_LEVELS("VIP Levels"),
    TAX_TYPES("tax types"),
    CHECK_TYPE_SNAPSHOTS("Check Type Snapshots"),
    ACCIDENT_REPORT_SOURCE("Accident Report Source"),
    LIABILITY_LEVELS("Liability Levels"),
    REASONS_FOR_UNCOVERED_INSURANCE("Reasons For Uncovered Insurance"),
    ACCIDENT_TYPES("Accident Types"),
    LEASING_QUOTATION_DURATIONS("Leasing Quotation Durations"),
    ESTIMATION_PARTIES("Estimation Parties"),
    STATEMENT_OBJECTION_REASONS("Statement Objection Reasons"),
    OCCUPATION_LEVEL("Occupation level"),
    FEEDBACK_TYPES("Feedback Types "),
    MEDIA("Media"),
    OTHER_CHARGES_TYPES("Other Charges Types"),
    REJECTION_REASONS("Rejection Reasons"),
    OPL_REQUEST_PERIODS("OPL Request Periods"),
    SALARIES("Salaries"),
    SELF_CONTRACT_CANCEL_REASONS("Self Contract Cancel Reasons"),
    LEASING_EXTRAS_TYPES("Leasing Extras Types"),
    ROAD_ASSISTANCE_DECLINE_REASONS("Road Assistance Decline Reasons"),
    REGISTRATION_REJECTION_REASONS("Registration Rejection Reasons"),
    THEFT_REQUEST_REJECTION_REASONS("Theft Request Rejection Reasons"),
    CANCEL_ACCIDENT_REASONS("Cancel Accident Reasons"),
    REASSIGN_VEHICLE_REASONS("Reassign Vehicle Reasons"),
    SLA_ACCIDENT_MAINTENANCE_LEVEL("SLA - Accident Maintenance Level"),
    SLA_MECHANICAL_MAINTENANCE_LEVEL("SLA - Mechanical Maintenance Level"),
    TRACKING_DEVICE_TYPES("Tracking Device Types"),
    TRACKING_REPORT_TYPES("Tracking Report Types"),
    TRACKING_EVENTS_LOG_REPORT_TYPES("Tracking Events Log Report Types"),
    BRANDS("Brands"),
    CUSTOMER_DATA_AGREEMENTS("Customer Data Agreements"),
    BANK_NAMES("Bank Names"),
    INSURANCE_DEPOSIT_TYPES("Insurance Deposit Types"),
    SLA_MISUSE_MAINTENANCE_LEVEL("SLA - Misuse Maintenance Level");

    private final String displayText;

    LookupTypes(String displayText) {
        this.displayText = displayText;
    }

    /**
     * Gets the display text for this lookup type.
     *
     * @return The display text string
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Finds a LookupType enum by its display text.
     * The comparison is case-sensitive and trims whitespace.
     *
     * @param displayText The display text to search for
     * @return The matching LookupType, or null if not found
     */
    public static LookupTypes findByDisplayText(String displayText) {
        if (displayText == null) {
            return null;
        }
        String trimmed = displayText.trim();
        for (LookupTypes type : values()) {
            if (type.displayText.equals(trimmed)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Finds a LookupType enum by its display text (case-insensitive).
     * The comparison ignores case and trims whitespace.
     *
     * @param displayText The display text to search for
     * @return The matching LookupType, or null if not found
     */
    public static LookupTypes findByDisplayTextIgnoreCase(String displayText) {
        if (displayText == null) {
            return null;
        }
        String trimmed = displayText.trim();
        for (LookupTypes type : values()) {
            if (type.displayText.equalsIgnoreCase(trimmed)) {
                return type;
            }
        }
        return null;
    }
}

