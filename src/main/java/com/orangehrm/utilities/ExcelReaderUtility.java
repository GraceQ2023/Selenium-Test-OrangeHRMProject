package com.orangehrm.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ClassName: ExcelReaderUtility
 * Package: com.orangehrm.utilities
 * Description:
 *
 * @Author Grace
 * @Create 17/7/2025 12:00 pm
 * Version 1.0
 */
public class ExcelReaderUtility {

    // Each row in the Excel sheet becomes an array of strings (String[])
    // All rows together are stored in a list (List<String[]>)
    public static List<String[]> getSheetData(String filePath, String sheetName) {

        // 'data' holds all the rows from the sheet.
        // each row is stored as a String[] (array of cell values)
        // all rows together are stored in a list (List<String[]>). e.g. data.get(0) → ["Admin", "admin123"], data.get(1) → ["User1", "pass456"]
        List<String[]> data = new ArrayList<>();
        try {
            // Read the Excel file
            FileInputStream fis = new FileInputStream(filePath);
            // Load the Excel workbook
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet: " + sheetName + " is not exists.");
            }

            // Loop through each row in the sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;  // skip the first row (usually header row)
                }

                // 'rowData' stores all cell values of the current row
                List<String> rowData = new ArrayList<>();
                // Loop through each cell in the row
                for (Cell cell : row) {
                    // convert cell value to String and add to rowData.  rowData = ["Admin", "admin123"];
                    rowData.add(getCellValue(cell));
                }

                // Convert rowData (List<String>) into String[] and add to data
                // Every Excel row → becomes rowData → converted to String[] → added to data
                // Create a new array of type String with size 0, Java will internally create a new array of the correct size based on rowData.size().
                data.add(rowData.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // helper method converts any Excel cell value to String
    private static String getCellValue(Cell cell){
        if(cell==null){
            return "";
        }
        switch(cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // if it's a date, format as date string
                if(DateUtil.isCellDateFormatted(cell))
                    return cell.getDateCellValue().toString();
                // if it's a number, convert to int then to String
                return String.valueOf((int)cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return " ";
        }
    }
}

//rowData stores one row of cell values (as a list of strings).
//data stores all rows of the sheet, so it's a list of rows.
