package com.util;

import java.security.SecureRandom;

/**
 * Utility class for string operations.
 */
public class StringUtil {

    private static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Gets value or returns empty string if null or empty.
     * Trims the value if it's not null and not empty.
     *
     * @param value The string value to process
     * @return Trimmed value if not null and not empty, otherwise empty string
     */
    public static String getValueOrEmpty(String value) {
        return value != null && !value.trim().isEmpty() ? value.trim() : "";
    }

    /**
     * Generates a random alphanumeric string of the specified length and prepends the fixed string.
     *
     * @param prefix The fixed string to prepend to the generated random string
     * @param length The length of the random string to generate
     * @return The prefix followed by the random string
     * @throws IllegalArgumentException if length is negative
     */
    public static String generateRandomStringWithPrefix(String prefix, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }
        
        if (prefix == null) {
            prefix = "";
        }
        
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            randomString.append(ALPHANUMERIC.charAt(index));
        }
        
        return prefix + randomString.toString();
    }

    /**
     * Generates a random numeric string of the specified length.
     *
     * @param length The length of the random number string to generate
     * @return A string containing random digits (0-9) of the specified length
     * @throws IllegalArgumentException if length is negative or zero
     */
    public static String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        
        StringBuilder randomNumber = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // 0-9
            randomNumber.append(digit);
        }
        
        return randomNumber.toString();
    }
}

