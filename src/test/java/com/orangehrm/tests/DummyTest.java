package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.SkipException;
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
public class DummyTest extends BaseClass {

    @Test
    public void dummyTest(){
        // ExtentManager.startTestEntry("DummyTest1 Test", this.getClass().getSimpleName()); --> This has been implemented in TestListener class
        String pageTitle = getDriver().getTitle();
        System.out.println(pageTitle);
        ExtentManager.logStep("Verifying the title");
        assert pageTitle.equals("OrangeHRM"):"Test failed - Title not matching";
        System.out.println("Test passed - Title is matching");
        // ExtentManager.logSkip("This case is skipped");
        throw new SkipException("Skipping the test as part of testing");  //Immediately stops executing that test, triggers onTestSkipped() listener.
    }

}
