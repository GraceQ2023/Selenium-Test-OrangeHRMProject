package com.orangehrm.dataprovider;

import com.orangehrm.utilities.ExcelReaderUtility;
import org.testng.annotations.DataProvider;

import java.util.List;

/**
 * ClassName: DataProviders
 * Package: com.orangehrm.utilities
 * Description:
 *
 * @Author Grace
 * @Create 17/7/2025 12:56 pm
 * Version 1.0
 */
public class DataProviders {
    private static final String FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/testdata/TestData.xlsx";

    @DataProvider(name="validLoginData")
    public static Object[][] validLoginData(){
        return getSheetData("validLoginData");
    }

    @DataProvider(name="invalidLoginData")
    public static Object[][] invalidLoginData(){
        return getSheetData("invalidLoginData");
    }

    // it reads a sheet from an Excel file, and converts its rows into a 2D array (Object[][]),format required by TestNG @DataProvider.
    // Object[][] - 2D array of data from the sheet
    private static Object[][] getSheetData(String sheetName){
        List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

        // Create 2D Object[][] array with the same number of rows and columns
        Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

        //  Copy from sheetData (the list of String arrays) into the Object 2D array - data
        for(int i=0; i<sheetData.size(); i++){
            data[i] = sheetData.get(i); //Convert each row (String[]) into Object[]
        }
        return data;
        // data = {{"Admin", "admin123"},
        //         {"User1", "pass123"}
        //        }
    }

}


// ExcelReaderUtility.getSheetData(...) returns a list of string arrays → one string array per row.
// Example: List<String[]> = [ ["Admin", "admin123"], ["User1", "pass123"] ]

// We want to convert that List<String[]> into a 2D object array:
// Object[][] = { {"Admin", "admin123"}, {"User1", "pass123"} }

// "We get all rows from the Excel sheet as a list of arrays.
// Then we convert that list into a 2D array (Object[][])
// and return it, so TestNG can feed this data to our test method."


// List<String[]> = Each row in Excel (excluding the header) is converted into a String[]. All rows together form a List<String[]>.

// Object[][] = TestNG needs the test data as a 2D array (like a table), so we convert the list of rows into this format.

// getSheetData() reads one sheet and converts the row data into a usable format for TestNG's @DataProvider.
