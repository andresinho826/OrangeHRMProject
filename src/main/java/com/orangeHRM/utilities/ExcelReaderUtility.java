package com.orangeHRM.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtility {

    public static List<String[]> getSheetData(String filePath, String sheetName) throws IOException {
        // data variable to defined as a list of string arrays
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " does not exist in " + filePath);
            }
            // iterate through each row in the sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip header row
                }
                // reade all cells in the row
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell));
                }
                // convert rowData to String array and add to data list
                data.add(rowData.toArray(new String[0]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

        //ExcelReader excelReader = new ExcelReader(filePath);
        //return excelReader.getDataFromSheet(sheetName);
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }

    }
}
