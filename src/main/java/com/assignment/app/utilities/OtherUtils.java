package com.assignment.app.utilities;

import com.assignment.app.pageobjectfactory.ParentSupportAccountCreationPage;
import org.openqa.selenium.*;

public class OtherUtils {
    public Boolean isCreateAccountFieldEnabled(WebDriver driver) {
        if (new ParentSupportAccountCreationPage(driver).getCreateAccount().getAttribute("style").contains("opacity: 1")) {
            System.out.println(new ParentSupportAccountCreationPage(driver).getCreateAccount().getAttribute("style"));
            return true;
        } else {
            return false;
        }

    }
}
