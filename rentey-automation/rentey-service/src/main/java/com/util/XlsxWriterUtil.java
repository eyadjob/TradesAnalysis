package com.util;

import com.pojo.CustomerCsvData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for writing customer import results to XLSX files.
 */
@Component
public class XlsxWriterUtil {

    private static final Logger logger = LoggerFactory.getLogger(XlsxWriterUtil.class);

    /**
     * Writes customer import results to an XLSX file.
     * The file contains all original CSV data plus response code and message columns.
     *
     * @param customerDataList List of customer CSV data
     * @param responseCodes List of response codes (HTTP status codes or error codes)
     * @param responseMessages List of response messages
     * @param outputDirectory Directory where the XLSX file will be saved
     * @return Path to the created XLSX file
     */
    public String writeImportResultsToXlsx(
            List<CustomerCsvData> customerDataList,
            List<String> responseCodes,
            List<String> responseMessages,
            String outputDirectory) {
        
        if (customerDataList == null || customerDataList.isEmpty()) {
            logger.warn("No customer data to write to XLSX file");
            return null;
        }

        // Ensure output directory exists
        Path outputPath = Paths.get(outputDirectory);
        try {
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
            }
        } catch (IOException e) {
            logger.error("Failed to create output directory: {}", outputDirectory, e);
            return null;
        }

        // Generate filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "customer_import_results_" + timestamp + ".xlsx";
        Path filePath = outputPath.resolve(fileName);

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {

            Sheet sheet = workbook.createSheet("Import Results");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create data row styles
            CellStyle successDataStyle = createSuccessDataStyle(workbook);
            CellStyle errorDataStyle = createErrorDataStyle(workbook);
            CellStyle defaultDataStyle = createDataStyle(workbook);

            // Create header row
            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            createHeaderRow(headerRow, headerStyle);

            // Create data rows with conditional styling based on response code
            for (int i = 0; i < customerDataList.size(); i++) {
                CustomerCsvData customerData = customerDataList.get(i);
                String responseCode = (i < responseCodes.size()) ? responseCodes.get(i) : "";
                String responseMessage = (i < responseMessages.size()) ? responseMessages.get(i) : "";

                Row dataRow = sheet.createRow(rowNum++);
                
                // Choose style based on response code
                CellStyle rowStyle;
                if ("200".equals(responseCode)) {
                    rowStyle = successDataStyle;
                } else if (responseCode != null && !responseCode.isEmpty() && !"200".equals(responseCode)) {
                    rowStyle = errorDataStyle;
                } else {
                    rowStyle = defaultDataStyle;
                }
                
                createDataRow(dataRow, customerData, responseCode, responseMessage, rowStyle);
            }

            // Auto-size columns
            autoSizeColumns(sheet);

            // Write to file
            workbook.write(outputStream);
            logger.info("Successfully wrote {} customer records to XLSX file: {}", 
                    customerDataList.size(), filePath.toAbsolutePath());

            return filePath.toAbsolutePath().toString();

        } catch (IOException e) {
            logger.error("Error writing XLSX file: {}", filePath.toAbsolutePath(), e);
            return null;
        }
    }

    /**
     * Creates the header row with all column names.
     */
    private void createHeaderRow(Row headerRow, CellStyle style) {
        String[] headers = {
            "FirstName", "SecondName", "FamilyName", "Nationality", "Gender",
            "BirthDate", "PrimaryPhone", "DocumentType", "DocumentNumber",
            "DocumentExpireDate", "DocumentIssueCountry", "licenseNo",
            "LicenseIssueCountry", "licenseExpiryDate", "MemberShip Level",
            "Importing Response Code", "Importing Response Message"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * Creates a data row with customer data and response information.
     */
    private void createDataRow(Row dataRow, CustomerCsvData customerData, 
                               String responseCode, String responseMessage, CellStyle style) {
        int colNum = 0;

        // Original CSV data
        setCellValue(dataRow, colNum++, customerData.firstName(), style);
        setCellValue(dataRow, colNum++, customerData.secondName(), style);
        setCellValue(dataRow, colNum++, customerData.familyName(), style);
        setCellValue(dataRow, colNum++, customerData.nationality(), style);
        setCellValue(dataRow, colNum++, customerData.gender(), style);
        setCellValue(dataRow, colNum++, customerData.birthDate(), style);
        setCellValue(dataRow, colNum++, customerData.primaryPhone(), style);
        setCellValue(dataRow, colNum++, customerData.documentType(), style);
        setCellValue(dataRow, colNum++, customerData.documentNumber(), style);
        setCellValue(dataRow, colNum++, customerData.documentExpireDate(), style);
        setCellValue(dataRow, colNum++, customerData.documentIssueCountry(), style);
        setCellValue(dataRow, colNum++, customerData.licenseNo(), style);
        setCellValue(dataRow, colNum++, customerData.licenseIssueCountry(), style);
        setCellValue(dataRow, colNum++, customerData.licenseExpiryDate(), style);
        setCellValue(dataRow, colNum++, customerData.membershipLevel(), style);

        // Response columns
        setCellValue(dataRow, colNum++, responseCode, style);
        setCellValue(dataRow, colNum++, responseMessage, style);
    }

    /**
     * Sets a cell value with the given style.
     */
    private void setCellValue(Row row, int colNum, String value, CellStyle style) {
        Cell cell = row.createCell(colNum);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    /**
     * Creates a header style with bold font and custom background color (#61CBF3).
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        
        // Set custom background color #61CBF3 (light blue)
        org.apache.poi.xssf.usermodel.XSSFColor headerColor = new org.apache.poi.xssf.usermodel.XSSFColor(
                new byte[]{(byte) 0x61, (byte) 0xCB, (byte) 0xF3}, null);
        ((org.apache.poi.xssf.usermodel.XSSFCellStyle) style).setFillForegroundColor(headerColor);
        
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Creates a data style for regular cells (centered text).
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Creates a success data style for records with response code 200 (green background #5AC135).
     */
    private CellStyle createSuccessDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Set custom background color #5AC135 (green)
        org.apache.poi.xssf.usermodel.XSSFColor successColor = new org.apache.poi.xssf.usermodel.XSSFColor(
                new byte[]{(byte) 0x5A, (byte) 0xC1, (byte) 0x35}, null);
        ((org.apache.poi.xssf.usermodel.XSSFCellStyle) style).setFillForegroundColor(successColor);
        
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Creates an error data style for records with response code other than 200 (red background #FFA3A3).
     */
    private CellStyle createErrorDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Set custom background color #FFA3A3 (light red/pink)
        org.apache.poi.xssf.usermodel.XSSFColor errorColor = new org.apache.poi.xssf.usermodel.XSSFColor(
                new byte[]{(byte) 0xFF, (byte) 0xA3, (byte) 0xA3}, null);
        ((org.apache.poi.xssf.usermodel.XSSFCellStyle) style).setFillForegroundColor(errorColor);
        
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Auto-sizes all columns in the sheet.
     */
    private void autoSizeColumns(Sheet sheet) {
        if (sheet.getRow(0) != null) {
            int lastColumn = sheet.getRow(0).getLastCellNum();
            for (int i = 0; i < lastColumn; i++) {
                sheet.autoSizeColumn(i);
                // Add some padding
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }
        }
    }
}

