package com.ui.cucumber;

import com.Entities.Response.CreateTestRunResponse;
import com.Entities.Response.GetTestResult;
import com.Entities.Request.AddTestResult;
import com.Entities.Request.CreateTestRun;
import com.Entities.Request.ShallowReference;
import com.ui.cucumber.Repo.HomePage;
import com.ui.cucumber.Repo.SettingsPage;
import com.backend.Constants;
import com.google.gson.Gson;
import com.backend.TestRunController;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by mac on 25/08/17.
 */
public class Browser {

    private WebDriver driver;
    private Properties prop;
    private HomePage homePage;
    private SettingsPage settings;
    TestRunController testRunController = new TestRunController();
    String base_url;
    Gson gson = new Gson();
    static boolean bSuite = false;
    static String testRunId;
    static int testCaseCount = 0;

    @Before
    public void setUp(){
        FileInputStream f = null;
        try{
            f = new FileInputStream(new File("src/test/Resources/com/ui/cucumber/Config.properties"));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        prop = new Properties();
        try{
            prop.load(f);
        }catch (IOException e){
            e.printStackTrace();
        }
        String browser = prop.getProperty("browser");
        switch (browser){
            case "chrome" :
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "firefox" :
                System.setProperty("webdriver.gecko.driver",prop.getProperty("driverExecutable") + "/geckodriver");
                driver = new FirefoxDriver();
                break;

            case "safari" :
                driver = new SafariDriver();
                break;

        }

        if(!bSuite){
            String executionTitle = "Automation Execution - " + RandomUtils.nextInt();
            CreateTestRunResponse response = testRunController.createNewTestRun(
                    getApiPath(Constants.AZURE_API, Constants.CREATE_TEST_RUN),
                    getCreateTestRunBody(executionTitle, Constants.TEST_PLAN_ID));
            testRunId = String.valueOf(response.getId());
            bSuite = true;
        }

        homePage = new HomePage(driver);
        settings = new SettingsPage(driver);
    }

    public WebDriver getDriver(){
        return driver;
    }

    public Properties getProp(){
        return prop;
    }

    public HomePage getHomePage(){
        return homePage;
    }

    @After
    public void tearDown(Scenario scenario) {
        String testResult = scenario.getStatus() == Status.PASSED ? "Passed" : "Failed";
        testCaseCount++;

        String azureTestCaseId = scenario.getSourceTagNames().toArray()[0].toString().substring(1);

        GetTestResult result = testRunController.getAllTestResults(getApiPath(Constants.AZURE_API, Constants.TEST_RESULT), testRunId);
        int totalCases = result.getValue().size();
        int resultId = result.getValue().stream().filter(e-> e.getTestPoint().getId().equals(azureTestCaseId)).findAny().get().getId();
        testRunController.updateTestCaseStatus(getApiPath(Constants.AZURE_API, Constants.TEST_RESULT),
                testRunId,
                getUpdateTestResultBody(testRunId, azureTestCaseId, testResult, resultId));

        if (testCaseCount==totalCases){
            testRunController.updateTestRunStatus(getApiPath(Constants.AZURE_API, Constants.UPDATE_TEST_RUN),
                    testRunId,
                    getCompleteTestRunBody());
        }

        driver.quit();
    }

    public String getApiPath(String url, String path){
        base_url = prop.getProperty(url + "_BASE_URL");
        return base_url + path;
    }

    private String getUpdateTestResultBody(String runId, String pointId, String outcome, int resultId){
        ShallowReference testRun = ShallowReference.builder().id(runId).build();
        ShallowReference testPoint = ShallowReference.builder().id(pointId).build();
        List<AddTestResult> addTestResult = List.of(AddTestResult.builder().id(resultId).testRun(testRun).testPoint(testPoint).outcome(outcome).build());
        return gson.toJson(addTestResult);
    }

    private String getCompleteTestRunBody(){
        CreateTestRun updateTestRun = CreateTestRun.builder().state("Completed").build();
        return gson.toJson(updateTestRun);
    }

    private String getCreateTestRunBody(String name, String planId){
        ShallowReference plan = ShallowReference.builder().id(planId).build();
        CreateTestRun createTestRun = CreateTestRun.builder().name(name).plan(plan).pointIds(List.of(1,2)).build();
        return gson.toJson(createTestRun);
    }
}
