package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for date formatting operations.
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * Formats date of birth to ISO format: YYYY-MM-DDTHH:mm:ss+03:00
     * If the date already includes time, returns it with timezone +03:00.
     * If only date is provided (without time), appends T00:00:00+03:00.
     * 
     * @param dateStr The date string to format
     * @return Formatted date string in ISO format with timezone +03:00
     */
    public static String formatDateToRenteyFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "1999-08-26T00:00:00+03:00"; // Default date with time
        }
        
        String trimmed = dateStr.trim();
        
        // If already in ISO format with time (contains T)
        if (trimmed.contains("T")) {
            // Extract date and time part (before timezone if exists)
            String dateTimePart;
            if (trimmed.contains("+")) {
                dateTimePart = trimmed.substring(0, trimmed.indexOf("+"));
            } else if (trimmed.endsWith("Z")) {
                dateTimePart = trimmed.substring(0, trimmed.length() - 1);
            } else {
                dateTimePart = trimmed;
            }
            
            // Ensure format is YYYY-MM-DDTHH:mm:ss, then add timezone
            if (dateTimePart.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
                return dateTimePart + "+03:00";
            }
            // If format is close but missing seconds, add them
            if (dateTimePart.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                return dateTimePart + ":00+03:00";
            }
            // Return as is if format is already correct
            return trimmed;
        }
        
        // If only date is provided (YYYY-MM-DD format)
        if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return trimmed + "T00:00:00+03:00";
        }
        
        // YYYY/MM/DD format (e.g., 1989/02/28)
        if (trimmed.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
            String[] parts = trimmed.split("/");
            if (parts.length == 3) {
                String year = parts[0];
                String month = String.format("%02d", Integer.parseInt(parts[1]));
                String day = String.format("%02d", Integer.parseInt(parts[2]));
                return year + "-" + month + "-" + day + "T00:00:00+03:00";
            }
        }
        
        // Try other common date formats
        // DD/MM/YYYY format
        if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            String[] parts = trimmed.split("/");
            if (parts.length == 3) {
                String year = parts[2];
                String month = String.format("%02d", Integer.parseInt(parts[1]));
                String day = String.format("%02d", Integer.parseInt(parts[0]));
                return year + "-" + month + "-" + day + "T00:00:00+03:00";
            }
        }
        
        // If format is unknown, log warning and return default
        logger.warn("Unknown date format: {}, using default", dateStr);
        return "1999-08-26T00:00:00+03:00";
    }
}

