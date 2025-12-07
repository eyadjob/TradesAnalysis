package com.util;

import com.pojo.CustomerCsvData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Utility class for importing CSV files and storing values in a HashMap.
 * Reads all CSV files from the customersToImport directory.
 */
@Component
public class CustomerCsvImportUtil {

    private static final Logger logger = LoggerFactory.getLogger(CustomerCsvImportUtil.class);
    
    @Value("${csv.import.directory}")
    private String csvDirectory;

    /**
     * Imports all CSV files from the customersToImport directory and returns a list of CustomerCsvData objects.
     * 
     * @return List of CustomerCsvData objects containing customer data from all CSV files
     */
    public List<CustomerCsvData> getCsvFiles() {
        List<CustomerCsvData> customerDataList = new ArrayList<>();
        Path csvDirectoryPath = Paths.get(System.getProperty("user.dir") +"\\"+ csvDirectory);

        try {
            // Check if directory exists
            if (!Files.exists(csvDirectoryPath) || !Files.isDirectory(csvDirectoryPath)) {
                logger.warn("CSV directory does not exist: {}", csvDirectoryPath.toAbsolutePath());
                return customerDataList;
            }

            // Get all CSV files in the directory
            File[] csvFiles = csvDirectoryPath.toFile().listFiles((dir, name) -> 
                    name.toLowerCase().endsWith(".csv"));

            if (csvFiles == null || csvFiles.length == 0) {
                logger.warn("No CSV files found in directory: {}", csvDirectoryPath.toAbsolutePath());
                return customerDataList;
            }

            logger.info("Found {} CSV file(s) to import", csvFiles.length);

            // Process each CSV file
            for (File csvFile : csvFiles) {
                logger.info("Processing CSV file: {}", csvFile.getName());
                List<CustomerCsvData> fileData = importCsvFileAsCustomerData(csvFile);
                customerDataList.addAll(fileData);
                logger.info("Imported {} customer records from file: {}", fileData.size(), csvFile.getName());
            }

            logger.info("Total customer records imported: {}", customerDataList.size());
            return customerDataList;

        } catch (Exception e) {
            logger.error("Error importing CSV files from directory: {}", csvDirectoryPath.toAbsolutePath(), e);
            return customerDataList;
        }
    }

    /**
     * Imports a single CSV file and returns a list of CustomerCsvData objects.
     * 
     * @param csvFile The CSV file to import
     * @return List of CustomerCsvData objects
     */
    public List<CustomerCsvData> importCsvFileAsCustomerData(File csvFile) {
        List<CustomerCsvData> customerDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int rowNumber = 0;
            List<String> headers = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                List<String> values = parseCsvLine(line);

                if (rowNumber == 0) {
                    // First row contains headers
                    headers = values;
                    logger.debug("Found {} columns in file: {}", headers.size(), csvFile.getName());
                } else {
                    // Data rows - create CustomerCsvData object
                    if (headers != null) {
                        Map<String, String> rowData = new HashMap<>();
                        for (int i = 0; i < values.size() && i < headers.size(); i++) {
                            String header = headers.get(i).trim();
                            String value = values.get(i);
                            rowData.put(header, value != null ? value.trim() : "");
                        }
                        
                        try {
                            CustomerCsvData customerData = CustomerCsvData.fromMap(rowData);
                            customerDataList.add(customerData);
                        } catch (Exception e) {
                            logger.warn("Error creating CustomerCsvData from row {}: {}", rowNumber, e.getMessage());
                        }
                    }
                }
                rowNumber++;
            }

            logger.info("Imported {} customer records from file: {}", customerDataList.size(), csvFile.getName());

        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", csvFile.getAbsolutePath(), e);
        }

        return customerDataList;
    }

    /**
     * Imports a single CSV file and returns a HashMap.
     * 
     * @param csvFile The CSV file to import
     * @return HashMap with keys in format: fileName_rowNumber_columnName
     */
    public Map<String, String> importCsvFile(File csvFile) {
        Map<String, String> csvData = new HashMap<>();
        String fileName = csvFile.getName().replace(".csv", "");

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int rowNumber = 0;
            List<String> headers = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                List<String> values = parseCsvLine(line);

                if (rowNumber == 0) {
                    // First row contains headers
                    headers = values;
                    logger.debug("Found {} columns in file: {}", headers.size(), csvFile.getName());
                } else {
                    // Data rows
                    if (headers != null) {
                        for (int i = 0; i < values.size() && i < headers.size(); i++) {
                            String key = String.format("%s_%d_%s", fileName, rowNumber, headers.get(i));
                            String value = values.get(i);
                            csvData.put(key, value.trim());
                        }
                    }
                }
                rowNumber++;
            }

            logger.info("Imported {} rows from file: {}", rowNumber - 1, csvFile.getName());

        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", csvFile.getAbsolutePath(), e);
        }

        return csvData;
    }

    /**
     * Parses a CSV line, handling quoted values and commas within quotes.
     * 
     * @param line The CSV line to parse
     * @return List of parsed values
     */
    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentValue.append('"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        // Add the last field
        values.add(currentValue.toString().trim());

        return values;
    }

    /**
     * Gets all CSV data grouped by file name.
     * 
     * @return Map where key is file name and value is a map of row data
     */
    public Map<String, Map<String, String>> importCsvFilesGroupedByFile() {
        Map<String, Map<String, String>> groupedData = new HashMap<>();
        Path csvDirectoryPath = Paths.get(csvDirectory);

        try {
            if (!Files.exists(csvDirectoryPath) || !Files.isDirectory(csvDirectoryPath)) {
                logger.warn("CSV directory does not exist: {}", csvDirectoryPath.toAbsolutePath());
                return groupedData;
            }

            File[] csvFiles = csvDirectoryPath.toFile().listFiles((dir, name) -> 
                    name.toLowerCase().endsWith(".csv"));

            if (csvFiles == null || csvFiles.length == 0) {
                logger.warn("No CSV files found in directory: {}", csvDirectoryPath.toAbsolutePath());
                return groupedData;
            }

            for (File csvFile : csvFiles) {
                String fileName = csvFile.getName().replace(".csv", "");
                Map<String, String> fileData = importCsvFile(fileName);
                groupedData.put(fileName, fileData);
            }

        } catch (Exception e) {
            logger.error("Error importing CSV files from directory: {}", csvDirectoryPath.toAbsolutePath(), e);
        }

        return groupedData;
    }

    /**
     * Imports a CSV file by name and returns a HashMap.
     * 
     * @param fileName The name of the CSV file (without .csv extension)
     * @return HashMap with keys in format: rowNumber_columnName
     */
    public Map<String, String> importCsvFile(String fileName) {
        Path csvFile = Paths.get(csvDirectory, fileName + ".csv");
        return importCsvFile(csvFile.toFile());
    }
}

