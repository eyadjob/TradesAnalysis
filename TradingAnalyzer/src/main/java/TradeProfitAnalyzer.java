import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Apache POI imports for Excel formatting
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import java.awt.Color;

public class TradeProfitAnalyzer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    static class Trade {
        LocalDateTime openTime;
        LocalDateTime closeTime;
        String type;
        double profit;

        public Trade(LocalDateTime openTime, LocalDateTime closeTime, String type, double profit) {
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.type = type;
            this.profit = profit;
        }
    }

    public static void main(String[] args) {
        String directoryPath = "D:\\\\TradesAnalysis";

        try {
            // Get all CSV files in the directory
            File directory = new File(directoryPath);
            File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if (csvFiles == null || csvFiles.length == 0) {
                System.out.println("No CSV files found in directory: " + directoryPath);
                return;
            }

            System.out.println("Found " + csvFiles.length + " CSV file(s) to process:");

            for (File csvFile : csvFiles) {
                String inputFile = csvFile.getAbsolutePath();
                String outputFile = inputFile.replace(".csv", ".xlsx");

                System.out.println("Processing: " + csvFile.getName());

                try {
                    Map<String, Double> monthlyProfits = analyzeTrades(inputFile);
                    writeResultsToExcel(monthlyProfits, outputFile);
                    System.out.println("  ✓ Analysis complete! Results saved to: " + outputFile);

                } catch (IOException e) {
                    System.err.println("  ✗ Error processing file " + csvFile.getName() + ": " + e.getMessage());
                }
            }

            System.out.println("\nAll files processed successfully!");

        } catch (Exception e) {
            System.err.println("Error processing directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<String, Double> analyzeTrades(String inputFilePath) throws IOException {
        Map<String, Double> monthlyProfits = new TreeMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip empty lines and header
                if (line.trim().isEmpty() || isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Parse CSV line (semicolon separated)
                String[] fields = line.split(";");

                // Skip balance operations, buy limits, and invalid lines
                if (fields.length < 8 || "Balance".equals(fields[1]) || "Buy Limit".equals(fields[1])) {
                    continue;
                }

                try {
                    // Parse open time (field 0 is always open time)
                    LocalDateTime openTime = parseDateTime(fields[0]);
                    String type = fields[1];

                    // Find the close time - look for a field that matches the date pattern
                    LocalDateTime closeTime = null;
                    for (int i = 6; i < Math.min(fields.length, 9); i++) {
                        if (isValidDateTime(fields[i])) {
                            closeTime = parseDateTime(fields[i]);
                            break;
                        }
                    }

                    // Find the profit - look for a numeric field in the profit position range
                    double profit = 0.0;
                    for (int i = 10; i < Math.min(fields.length, 13); i++) {
                        if (isNumeric(fields[i])) {
                            profit = parseDouble(fields[i]);
                            break;
                        }
                    }

                    // Skip if we couldn't find close time
                    if (closeTime == null) {
                        continue;
                    }

                    // Use closing date for monthly grouping
                    String monthKey = getMonthKey(closeTime);

                    // Add profit to monthly total
                    monthlyProfits.merge(monthKey, profit, Double::sum);

                } catch (Exception e) {
                    System.err.println("Skipping invalid line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        return monthlyProfits;
    }

    private static boolean isValidDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            parseDateTime(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isNumeric(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void writeResultsToExcel(Map<String, Double> monthlyProfits, String outputFilePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Profits");

            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle positiveProfitStyle = createPositiveProfitStyle(workbook);
            CellStyle negativeProfitStyle = createNegativeProfitStyle(workbook);
            CellStyle defaultStyle = createDefaultCellStyle(workbook);
            CellStyle summaryStyle = createSummaryStyle(workbook);

            // Create title row with filename (without .csv)
            String fileName = outputFilePath.substring(outputFilePath.lastIndexOf("\\") + 1);
            fileName = fileName.replace(".xlsx", "");

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(fileName);
            titleCell.setCellStyle(titleStyle);

            // Merge cells A1:B1 for the title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

            // Create header row
            Row headerRow = sheet.createRow(1);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Month");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Net Profit");
            headerCell2.setCellStyle(headerStyle);

            // Write data rows and calculate totals
            int rowNum = 2;
            double totalProfits = 0.0;
            int monthCount = 0;

            for (Map.Entry<String, Double> entry : monthlyProfits.entrySet()) {
                Row row = sheet.createRow(rowNum++);

                // Month column
                Cell monthCell = row.createCell(0);
                monthCell.setCellValue(entry.getKey());
                monthCell.setCellStyle(defaultStyle);

                // Net Profit column
                Cell profitCell = row.createCell(1);
                double profit = entry.getValue();
                profitCell.setCellValue(profit);

                // Apply conditional formatting based on profit value
                if (profit < 0) {
                    profitCell.setCellStyle(negativeProfitStyle);
                } else {
                    profitCell.setCellStyle(positiveProfitStyle);
                }

                // Update totals
                totalProfits += profit;
                monthCount++;
            }

            // Add summary row
            Row summaryRow = sheet.createRow(rowNum);

            // Months Count cell
            Cell monthCountCell = summaryRow.createCell(0);
            monthCountCell.setCellValue("Months Count: " + monthCount);
            monthCountCell.setCellStyle(summaryStyle);

            // Total Profits cell
            Cell totalProfitsCell = summaryRow.createCell(1);
            totalProfitsCell.setCellValue("Total Profits: " + String.format("%.2f", totalProfits));
            totalProfitsCell.setCellStyle(summaryStyle);

            // Auto-size columns
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Write the workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        // Set font to Aptos Narrow, size 14 (2 pixels bigger than header), bold
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);

        // Set background color to #83CCEB (same as header)
        style.setFillForegroundColor(new XSSFColor(new Color(131, 204, 235), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set centered alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setFont(font);

        return style;
    }


    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        // Set font to Aptos Narrow, size 12, bold
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);

        // Set background color to #83CCEB
        style.setFillForegroundColor(new XSSFColor(new Color(131, 204, 235), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set centered alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setFont(font);

        return style;
    }

    private static CellStyle createPositiveProfitStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 11);

        // Green background for positive profits
        style.setFillForegroundColor(new XSSFColor(new Color(0, 255, 0), null)); // Pure green
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

        return style;
    }

    private static CellStyle createNegativeProfitStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 11);

        // Red background for negative profits
        style.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0), null)); // Pure red
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

        return style;
    }

    private static CellStyle createDefaultCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 11);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private static CellStyle createSummaryStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        // Set font to Aptos Narrow, size 11, bold
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);

        // Set background color to #F1A983
        style.setFillForegroundColor(new XSSFColor(new Color(241, 169, 131), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);

        return style;
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty date time string");
        }
        return LocalDateTime.parse(dateTimeStr.trim(), DATE_FORMATTER);
    }

    private static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value.trim());
    }

    private static String getMonthKey(LocalDateTime dateTime) {
        return String.format("%04d.%02d", dateTime.getYear(), dateTime.getMonthValue());
    }
}