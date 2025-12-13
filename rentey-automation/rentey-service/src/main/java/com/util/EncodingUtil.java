package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for encoding and decoding operations.
 */
public class EncodingUtil {

    private static final Logger logger = LoggerFactory.getLogger(EncodingUtil.class);

    /**
     * Decodes a URL-encoded string if it's already encoded to prevent double encoding.
     * If the string contains URL-encoded characters (%), it will be decoded.
     * If decoding fails, the original value is returned.
     *
     * @param encodedValue The potentially URL-encoded string to decode.
     * @return The decoded string, or the original value if decoding fails or is not needed.
     */
    public static String decodeIfEncoded(String encodedValue) {
        if (encodedValue == null || encodedValue.isEmpty()) {
            return encodedValue;
        }

        try {
            // Check if the string contains URL-encoded characters (starts with %)
            if (encodedValue.contains("%")) {
                return URLDecoder.decode(encodedValue, StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            // If decoding fails, use the original value
            logger.warn("Failed to decode request parameter, using original value: {}", e.getMessage());
        }

        return encodedValue;
    }

    /**
     * Encodes a string for use in URL query parameters with single quotes and double-encoded spaces.
     * Wraps the string in single quotes and URL encodes it, ensuring spaces are double-encoded.
     * 
     * Example: "Y G R 4515" -> "%27Y%2520G%2520R%25204515%27"
     * 
     * @param value The string to encode (e.g., plate number).
     * @return The encoded string wrapped in single quotes with double-encoded spaces.
     */
    public static String encodeWithQuotesAndDoubleEncodedSpaces(String value) {
        if (value == null) {
            return "";
        }
        
        // Wrap in single quotes
        String quoted =  value;
        
        // First, replace spaces with %20 to prepare for double encoding
        String withSpacesEncoded = quoted.replace(" ", "%20");
        withSpacesEncoded = withSpacesEncoded.replace("'", "%27");

        // URL encode the entire string
        // This will encode: ' as %27, and %20 (already encoded spaces) as %2520
        try {
            return URLEncoder.encode(withSpacesEncoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("Failed to encode string, using original value: {}", e.getMessage());
            return quoted;
        }
    }

    public static String encodePlateNumberForKendo(String plateNumber) {
        return "%27"+plateNumber.replace(" ", "%2520")+"%27";
    }
}

