package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public final class AuthHelper {
    private AuthHelper() {}

    public static void loginSmember(WebDriver driver, String baseUrl) {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Thiếu datn-test.properties (datn.phone/datn.password) hoặc biến môi trường DATN_PHONE/DATN_PASSWORD.");
        }
        driver.get(baseUrl + "smember");
        LoginPage loginPage = new LoginPage(driver);
        if (!loginPage.isLoginFormVisible()) {
            driver.get(baseUrl);
            HomePage home = new HomePage(driver);
            if (home.isUserLoggedIn()) {
                return;
            }
            driver.get(baseUrl + "smember");
        }
        loginPage.enterUsername(TestCredentials.phone());
        loginPage.enterPassword(TestCredentials.password());
        loginPage.clickLoginButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(35));
        wait.until(d -> {
            try {
                return new HomePage(d).isUserLoggedIn();
            } catch (Exception ex) {
                return false;
            }
        });
    }
}
