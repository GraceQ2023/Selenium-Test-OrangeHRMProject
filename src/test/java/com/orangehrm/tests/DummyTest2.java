package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.annotations.Test;

/**
 * ClassName: DummyTest
 * Package: com.orangehrm.tests
 * Description:
 *
 * @Author Grace
 * @Create 26/6/2025 3:43 pm
 * Version 1.0
 */
public class DummyTest2 extends BaseClass {

    @Test
    public void dummyTest2(){
        // ExtentManager.startTestEntry("DummyTest2 Test", this.getClass().getSimpleName());  --> This has been implemented in TestListener class
        String pageTitle = getDriver().getTitle();
        System.out.println(pageTitle);
        ExtentManager.logStep("Verifying the title");
        assert pageTitle.equals("OrangeHRM"):"Test failed - Title not matching";

        System.out.println("Test 2 passed - Title is matching");
        ExtentManager.logStep("Validation successfully");
    }

}
