package com.orangeHRM.test;

import com.orangeHRM.base.BaseClass;
import org.testng.annotations.Test;

import java.util.Objects;

public class DummyTest2 extends BaseClass {

    @Test
    public void dummyTest2(){
        String title = driver.getTitle();
        assert Objects.equals(title, "OrangeHRM") : "Test Failed - Title is not matching";

        System.out.println("Test passed - Title is Matching");
    }

}
