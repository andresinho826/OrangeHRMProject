package com.orangeHRM.test;

import com.orangeHRM.base.BaseClass;
import org.testng.annotations.Test;

import java.util.Objects;

public class DummyTest extends BaseClass {

    @Test
    public void dummyTest(){
        //String title = driver.getTitle();
        String title = getDriver().getTitle();
        assert Objects.equals(title, "OrangeHRM") : "Test Failed - Title is not matching";

        System.out.println("Test passed - Title is Matching");
    }

}
