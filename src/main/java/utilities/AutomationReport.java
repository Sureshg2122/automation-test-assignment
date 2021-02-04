package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

// simple report using extent framework

public class AutomationReport {

    static ExtentReports extent;

    public static ExtentReports getReportConfig() {
        extent = new ExtentReports();

        // To report all test cases
        ExtentSparkReporter reportAllTests = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\HTMLReport\\report.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.EXCEPTION})
                .apply();

        // To report only failure
        ExtentSparkReporter reportFailedTests = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\HTMLReport\\failureReport.html")
                .filter()
                .statusFilter()
                .as(new Status[]{Status.FAIL})
                .apply();

        reportFailedTests.viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.EXCEPTION})
                .apply();

        reportAllTests.config().setReportName("Web Automation Report");

        reportFailedTests.config().setReportName("Web Automation Report");

        extent.attachReporter(reportAllTests, reportFailedTests);
        extent.setSystemInfo("Author", "Suresh Gajendran");
        extent.setSystemInfo("Owner", "Suresh Gajendran");
        extent.setSystemInfo("OS", "Windows_10.1");
        return extent;
    }

}
