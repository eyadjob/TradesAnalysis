package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
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
}

