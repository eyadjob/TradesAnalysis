package com.util;

import com.annotation.LogExecutionTime;
import com.services.ImportCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(PropertyManager.class);

    @LogExecutionTime
    @Cacheable(cacheNames = "propertiesCache", value = "6Hours", keyGenerator = "AutoKeyGenerator")
    public static Map<String, String> loadPropertyFileIntoMap(String propertiesFileName) {
        Map<String, String> map = new HashMap<>();
        Properties properties = new Properties();

        try (InputStream inputStream = ImportCustomerService.class.getClassLoader()
                .getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                logger.error(propertiesFileName + " file not found in classpath");
                return map;
            }
            properties.load(inputStream);

            for (String key : properties.stringPropertyNames()) {
                map.put(key, properties.getProperty(key));
            }

            logger.info("Loaded {} "+propertiesFileName+" mappings from properties file", map.size());
        } catch (IOException e) {
            logger.error("Error loading "+propertiesFileName+" file", e);
        }

        return map;
    }
}
