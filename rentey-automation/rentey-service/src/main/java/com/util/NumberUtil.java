package com.util;

import java.util.Random;

/**
 * Utility class for number-related operations.
 */
public class NumberUtil {

    private static final Random random = new Random();

    /**
     * Generate a random numeric string of specified length.
     * 
     * @param length The length of the random numeric string to generate
     * @return A string containing random digits (0-9) of the specified length
     * @throws IllegalArgumentException if length is less than or equal to 0
     */
    public static String generateRandomNumericString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}

