package com.orangeHRM.test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.Objects;

public class DummyTest extends BaseClass {

    @Test
    public void dummyTest(){
        //String title = driver.getTitle();
        ExtentManager.startTest("Dummy test");
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verify Title Test");
        assert Objects.equals(title, "OrangeHRM") : "Test Failed - Title is not matching";

        System.out.println("Test passed - Title is Matching");
        ExtentManager.logSkip("Test Skipped");
        throw new SkipException("Skipping the test as part of testing");
    }

}
