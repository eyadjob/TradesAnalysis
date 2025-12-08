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
import java.util.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;

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
            int successCount = 0;
            int failedCount = 0;
            
            for (int i = 0; i < customerDataList.size(); i++) {
                CustomerCsvData customerData = customerDataList.get(i);
                String responseCode = (i < responseCodes.size()) ? responseCodes.get(i) : "";
                String responseMessage = (i < responseMessages.size()) ? responseMessages.get(i) : "";

                Row dataRow = sheet.createRow(rowNum++);
                
                // Choose style based on response code
                CellStyle rowStyle;
                if ("200".equals(responseCode)) {
                    rowStyle = successDataStyle;
                    successCount++;
                } else if (responseCode != null && !responseCode.isEmpty() && !"200".equals(responseCode)) {
                    rowStyle = errorDataStyle;
                    failedCount++;
                } else {
                    rowStyle = defaultDataStyle;
                    failedCount++; // Count empty/null as failed
                }
                
                createDataRow(dataRow, customerData, responseCode, responseMessage, rowStyle);
            }

            // Add summary rows
            CellStyle summaryStyle = createSummaryStyle(workbook);
            int summaryEndRow = addSummaryRows(sheet, rowNum, customerDataList.size(), successCount, failedCount, summaryStyle);

            // Group failed records by response message for chart
            Map<String, Long> failureMessageCounts = groupFailuresByMessage(
                    customerDataList, responseCodes, responseMessages);

            // Add pie chart for failure distribution
            if (!failureMessageCounts.isEmpty() && failedCount > 0) {
                createFailurePieChart((XSSFSheet) sheet, summaryEndRow + 2, failureMessageCounts);
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
     * Groups failed records by their response messages.
     * Normalizes duplicate identity number errors into a single category.
     */
    private Map<String, Long> groupFailuresByMessage(
            List<CustomerCsvData> customerDataList,
            List<String> responseCodes,
            List<String> responseMessages) {
        
        Map<String, Long> messageCounts = new LinkedHashMap<>();
        
        for (int i = 0; i < customerDataList.size(); i++) {
            String responseCode = (i < responseCodes.size()) ? responseCodes.get(i) : "";
            String responseMessage = (i < responseMessages.size()) ? responseMessages.get(i) : "";
            
            // Only count failed records (not 200)
            if (responseCode == null || responseCode.isEmpty() || !"200".equals(responseCode)) {
                String message = (responseMessage != null && !responseMessage.isEmpty()) 
                    ? responseMessage 
                    : "Unknown Error";
                
                // Normalize duplicate identity number errors
                message = normalizeErrorMessage(message);
                
                messageCounts.put(message, messageCounts.getOrDefault(message, 0L) + 1);
            }
        }
        
        return messageCounts;
    }

    /**
     * Normalizes error messages to group similar errors together.
     * Specifically, normalizes "Identity number X is duplicate" to "Identity number is duplicate".
     */
    private String normalizeErrorMessage(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        
        // Normalize duplicate identity number errors
        // Pattern: "Identity number <number> is duplicate" -> "Identity number is duplicate"
        String normalized = message.replaceAll(
            "(?i)Identity number\\s+\\d+\\s+is duplicate", 
            "Identity number is duplicate"
        );
        
        return normalized;
    }

    /**
     * Adds summary rows at the end of the sheet.
     * @return The row number after the last summary row
     */
    private int addSummaryRows(Sheet sheet, int startRowNum, int totalRecords, 
                               int successCount, int failedCount, CellStyle style) {
        int rowNum = startRowNum;
        
        // Add empty row for spacing
        sheet.createRow(rowNum++);
        
        // Total Customer Records Processed
        Row totalRow = sheet.createRow(rowNum++);
        Cell totalLabelCell = totalRow.createCell(0);
        totalLabelCell.setCellValue("Total Customer Records Processed");
        totalLabelCell.setCellStyle(style);
        Cell totalValueCell = totalRow.createCell(1);
        totalValueCell.setCellValue(totalRecords);
        totalValueCell.setCellStyle(style);
        
        // Failed Customer Records To Import
        Row failedRow = sheet.createRow(rowNum++);
        Cell failedLabelCell = failedRow.createCell(0);
        failedLabelCell.setCellValue("Failed Customer Records To Import");
        failedLabelCell.setCellStyle(style);
        Cell failedValueCell = failedRow.createCell(1);
        failedValueCell.setCellValue(failedCount);
        failedValueCell.setCellStyle(style);
        
        // Successfully Imported Customer Records
        Row successRow = sheet.createRow(rowNum++);
        Cell successLabelCell = successRow.createCell(0);
        successLabelCell.setCellValue("Successfully Imported Customer Records");
        successLabelCell.setCellStyle(style);
        Cell successValueCell = successRow.createCell(1);
        successValueCell.setCellValue(successCount);
        successValueCell.setCellStyle(style);
        
        return rowNum;
    }

    /**
     * Creates a summary style for summary rows (bold font, light gray background).
     */
    private CellStyle createSummaryStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        
        // Set light gray background color
        org.apache.poi.xssf.usermodel.XSSFColor summaryColor = new org.apache.poi.xssf.usermodel.XSSFColor(
                new byte[]{(byte) 0xE0, (byte) 0xE0, (byte) 0xE0}, null);
        ((org.apache.poi.xssf.usermodel.XSSFCellStyle) style).setFillForegroundColor(summaryColor);
        
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
     * Creates a pie chart showing failure distribution by response message.
     */
    private void createFailurePieChart(XSSFSheet sheet, int startRow, Map<String, Long> failureMessageCounts) {
        try {
            // Create data table for chart (Response Message and Count)
            int chartDataStartRow = startRow;
            int chartDataRow = chartDataStartRow;
            
            // Create header row for chart data
            Row headerRow = sheet.createRow(chartDataRow++);
            CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
            Cell messageHeader = headerRow.createCell(0);
            messageHeader.setCellValue("Response Message");
            messageHeader.setCellStyle(headerStyle);
            Cell countHeader = headerRow.createCell(1);
            countHeader.setCellValue("Count");
            countHeader.setCellStyle(headerStyle);
            
            // Create data rows
            int dataStartRow = chartDataRow;
            for (Map.Entry<String, Long> entry : failureMessageCounts.entrySet()) {
                Row dataRow = sheet.createRow(chartDataRow++);
                Cell messageCell = dataRow.createCell(0);
                messageCell.setCellValue(entry.getKey());
                Cell countCell = dataRow.createCell(1);
                countCell.setCellValue(entry.getValue());
            }
            int dataEndRow = chartDataRow - 1;
            
            // Create drawing and chart
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            
            // Position chart to the right of the data (starting at column 3, row startRow)
            XSSFClientAnchor anchor = drawing.createAnchor(
                    0, 0, 0, 0,
                    3, startRow,
                    11, startRow + 15);
            
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Failed Records by Response Message");
            chart.setTitleOverlay(false);
            
            // Define chart data sources
            XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(
                    sheet, new CellRangeAddress(dataStartRow, dataEndRow, 0, 0));
            XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(dataStartRow, dataEndRow, 1, 1));
            
            // Create the pie chart data
            XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
            XDDFChartData.Series series = data.addSeries(categories, values);
            series.setTitle("Failure Distribution", null);
            
            // Plot the chart with the data
            chart.plot(data);
            
            // Set legend position
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);
            
            logger.info("Created pie chart for failure distribution with {} distinct error messages", 
                    failureMessageCounts.size());
            
        } catch (Exception e) {
            logger.error("Error creating pie chart", e);
        }
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

