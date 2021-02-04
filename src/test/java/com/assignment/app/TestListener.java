package com.assignment.app;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.assignment.app.utilities.AutomationReport;

import java.io.File;
import java.io.IOException;

// Test Listener implementation to track the tests

public class TestListener implements ITestListener {

    ExtentReports extentReports = new AutomationReport().getReportConfig();

    // Thread safe config for parallel test execution using extent reports
    ThreadLocal<ExtentTest> threadSafeTestDetails = new ThreadLocal<ExtentTest>();


    @Override
    public void onTestStart(ITestResult iTestResult) {
        threadSafeTestDetails.set(extentReports.createTest(iTestResult.getMethod().getMethodName()));
        threadSafeTestDetails.get().log(Status.INFO, "Test Execution Started");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        threadSafeTestDetails.get().log(Status.PASS, "Test Execution Passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        // takes screenshot on test failure

        try {
            TakesScreenshot screenshot = ((TakesScreenshot) (WebDriver) iTestResult.getTestClass().getRealClass().getDeclaredField("driver").get(iTestResult.getInstance()));
            FileUtils.copyFile(screenshot.getScreenshotAs(OutputType.FILE), new File(System.getProperty("user.dir") + "\\reports\\failure-screenshot\\" + iTestResult.getMethod().getMethodName() + ".png"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        threadSafeTestDetails.get().log(Status.FAIL, "Test Execution Failed" + System.lineSeparator() + iTestResult.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        threadSafeTestDetails.get().log(Status.SKIP, "Test Execution Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        threadSafeTestDetails.get().log(Status.WARNING, "Test Partially Successful");
    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extentReports.flush();
    }
}
