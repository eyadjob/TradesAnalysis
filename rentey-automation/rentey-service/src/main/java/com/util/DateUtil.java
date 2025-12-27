package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    /**
     * Adds or subtracts days, hours, minutes, and seconds from a given date.
     * Positive values increase the date, negative values decrease it.
     * 
     * @param dateStr The date string in format: YYYY-MM-DDTHH:mm:ss.SSSSSSS (e.g., 2026-02-25T22:34:13.2989838)
     * @param days Number of days to add (positive) or subtract (negative)
     * @param hours Number of hours to add (positive) or subtract (negative)
     * @param minutes Number of minutes to add (positive) or subtract (negative)
     * @param seconds Number of seconds to add (positive) or subtract (negative)
     * @return Modified date string in the same format as input
     * @throws IllegalArgumentException if the date string format is invalid
     */
    public static String addTimeToDate(String dateStr, long days, long hours, long minutes, long seconds) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }

        String trimmed = dateStr.trim();
        
        try {
            // Parse the date string - ISO_LOCAL_DATE_TIME handles variable fractional seconds
            LocalDateTime dateTime = LocalDateTime.parse(trimmed);
            
            // Add/subtract the time components
            dateTime = dateTime.plusDays(days)
                              .plusHours(hours)
                              .plusMinutes(minutes)
                              .plusSeconds(seconds);
            
            // Determine the fractional seconds precision from the original string
            int fractionalDigits = 0;
            if (trimmed.contains(".")) {
                int dotIndex = trimmed.indexOf(".");
                int endIndex = trimmed.length();
                // Find where fractional seconds end (before any timezone or other suffix)
                for (int i = dotIndex + 1; i < trimmed.length(); i++) {
                    if (!Character.isDigit(trimmed.charAt(i))) {
                        endIndex = i;
                        break;
                    }
                }
                fractionalDigits = endIndex - dotIndex - 1;
            }
            
            // Format back with the same precision as the original
            String baseFormatted = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            if (fractionalDigits > 0) {
                // Preserve the same fractional seconds precision as the original
                if (baseFormatted.contains(".")) {
                    String[] parts = baseFormatted.split("\\.");
                    String fractionalPart = parts[1];
                    // Pad or truncate to match original precision
                    if (fractionalPart.length() >= fractionalDigits) {
                        fractionalPart = fractionalPart.substring(0, fractionalDigits);
                    } else {
                        // Pad with zeros to match original precision
                        fractionalPart = String.format("%-" + fractionalDigits + "s", fractionalPart).replace(' ', '0');
                    }
                    return parts[0] + "." + fractionalPart;
                } else {
                    // No fractional seconds in result, add zeros to match original precision
                    return baseFormatted + "." + "0".repeat(fractionalDigits);
                }
            } else {
                // No fractional seconds in original, format without them
                if (baseFormatted.contains(".")) {
                    return baseFormatted.substring(0, baseFormatted.indexOf("."));
                } else {
                    return baseFormatted;
                }
            }
            
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date string: {}", trimmed, e);
            throw new IllegalArgumentException("Invalid date format: " + trimmed, e);
        }
    }

    /**
     * Adds or subtracts days from a given date.
     * Positive values increase the date, negative values decrease it.
     * 
     * @param dateStr The date string in format: YYYY-MM-DDTHH:mm:ss.SSSSSSS (e.g., 2026-02-25T22:34:13.2989838)
     * @param days Number of days to add (positive) or subtract (negative)
     * @return Modified date string in the same format as input
     * @throws IllegalArgumentException if the date string format is invalid
     */
    public static String addTimeToDate(String dateStr, long days) {
        return addTimeToDate(dateStr, days, 0, 0, 0);
    }

    /**
     * Adds or subtracts days and hours from a given date.
     * Positive values increase the date, negative values decrease it.
     * 
     * @param dateStr The date string in format: YYYY-MM-DDTHH:mm:ss.SSSSSSS (e.g., 2026-02-25T22:34:13.2989838)
     * @param days Number of days to add (positive) or subtract (negative)
     * @param hours Number of hours to add (positive) or subtract (negative)
     * @return Modified date string in the same format as input
     * @throws IllegalArgumentException if the date string format is invalid
     */
    public static String addTimeToDate(String dateStr, long days, long hours) {
        return addTimeToDate(dateStr, days, hours, 0, 0);
    }

    /**
     * Adds or subtracts days, hours, and minutes from a given date.
     * Positive values increase the date, negative values decrease it.
     * 
     * @param dateStr The date string in format: YYYY-MM-DDTHH:mm:ss.SSSSSSS (e.g., 2026-02-25T22:34:13.2989838)
     * @param days Number of days to add (positive) or subtract (negative)
     * @param hours Number of hours to add (positive) or subtract (negative)
     * @param minutes Number of minutes to add (positive) or subtract (negative)
     * @return Modified date string in the same format as input
     * @throws IllegalArgumentException if the date string format is invalid
     */
    public static String addTimeToDate(String dateStr, long days, long hours, long minutes) {
        return addTimeToDate(dateStr, days, hours, minutes, 0);
    }
}

