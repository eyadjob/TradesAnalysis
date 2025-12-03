package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class for ObjectMapper operations.
 */
@Component
public class ObjectMapperUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtil.class);
    private final ObjectMapper objectMapper;

    public ObjectMapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Serializes an object to JSON string.
     * 
     * @param object The object to serialize
     * @return JSON string representation of the object
     * @throws RuntimeException if serialization fails
     */
    public String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.warn("Could not serialize object to JSON for logging: {}", e.getMessage());
            throw new RuntimeException("Failed to serialize object to JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Serializes an object to JSON string with logging.
     * Logs the serialized JSON at debug level.
     * 
     * @param object The object to serialize
     * @return JSON string representation of the object
     * @throws RuntimeException if serialization fails
     */
    public String toJsonStringWithLogging(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            logger.debug("Request Body: {}", json);
            return json;
        } catch (Exception e) {
            logger.warn("Could not serialize object to JSON for logging: {}", e.getMessage());
            throw new RuntimeException("Failed to serialize object to JSON: " + e.getMessage(), e);
        }
    }
}

