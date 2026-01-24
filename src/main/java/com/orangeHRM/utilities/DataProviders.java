package com.orangeHRM.utilities;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataProviders {

    private static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "src/test/resources/testdata/TestData.xlsx";

    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return getSheetData("validLoginData");
    }

    @DataProvider(name = "invalidLoginData")
    public static Object[][] inValidLoginData() {
        return getSheetData("invalidLoginData");
    }

    private static Object[][] getSheetData(String sheetName) {
        try {
            List<String[]> data = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
            Object[][] dataArray = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataArray[i] = data.get(i);
            }
            return dataArray;
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }
}
