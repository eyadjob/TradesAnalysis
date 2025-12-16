package com.enums;

/**
 * Enum representing customer document types.
 */
public enum CustomerDocumentType {
    NOT_ASSIGNED("Not assigned"),
    IDENTITY("Identity"),
    PASSPORT("Passport"),
    WORK_CARD("Work Card"),
    DRIVER_LICENSE("Driver License"),
    RESIDENCE("Residence"),
    SELFIE("Selfie");

    private final String displayText;

    CustomerDocumentType(String displayText) {
        this.displayText = displayText;
    }

    /**
     * Gets the display text for the document type.
     *
     * @return The display text
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Finds a CustomerDocumentType by its display text (case-insensitive).
     *
     * @param displayText The display text to search for
     * @return The matching CustomerDocumentType, or null if not found
     */
    public static CustomerDocumentType fromDisplayText(String displayText) {
        if (displayText == null) {
            return null;
        }
        for (CustomerDocumentType type : values()) {
            if (type.displayText.equalsIgnoreCase(displayText.trim())) {
                return type;
            }
        }
        return null;
    }
}

