package Login;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.AuthHelper;
import utils.BaseTest;
import utils.TestCredentials;

/**
 * CellphoneS — Đăng nhập: DN1–DN6.
 * Tài khoản mẫu Excel: 0967824921 / abc123 (điền trong {@code datn-test.properties}).
 */
public class LoginDn01To06Test extends BaseTest {
    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @Test(description = "DN1 — Hiển thị màn đăng nhập từ trang chủ")
    @Epic("Đăng nhập hệ thống")
    @Feature("UI")
    @Severity(SeverityLevel.CRITICAL)
    public void dn01_loginFormFromHome() {
        driver.get(baseUrl);
        HomePage homePage = new HomePage(driver);
        LoginPage fromHome = homePage.openLoginDialog();
        fromHome.clickLoginTabInModal();
        Assert.assertTrue(fromHome.isLoginFormVisible(), "Form đăng nhập trong popup.");
    }

    @Test(description = "DN2 — Đăng nhập thành công")
    @Epic("Đăng nhập hệ thống")
    @Feature("Thành công")
    @Severity(SeverityLevel.BLOCKER)
    public void dn02_loginSuccess() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties (0967824921 / abc123).");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl);
        Assert.assertTrue(new HomePage(driver).isUserLoggedIn(), "Đăng nhập thành công.");
    }

    @Test(description = "DN3 — Sai mật khẩu (đúng SĐT)")
    @Epic("Đăng nhập hệ thống")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dn03_wrongPassword() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        driver.get(baseUrl + "smember");
        loginPage.enterUsername(TestCredentials.phone());
        loginPage.enterPassword("abcd123");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                !loginPage.getErrorMessage().isBlank() || loginPage.isLoginFormVisible(),
                "Kỳ vọng thông báo lỗi hoặc giữ form.");
    }

    @Test(description = "DN4 — Số điện thoại chưa đăng ký")
    @Epic("Đăng nhập hệ thống")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dn04_unregisteredPhone() {
        driver.get(baseUrl + "smember");
        loginPage.enterUsername("0964904003");
        loginPage.enterPassword("abc123");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                loginPage.waitForToastContaining("Không tồn tại")
                        || !loginPage.getErrorMessage().isBlank()
                        || loginPage.isLoginFormVisible(),
                "Kỳ vọng báo không tồn tại tài khoản hoặc lỗi đăng nhập.");
    }

    @Test(description = "DN5 — Bỏ trống số điện thoại")
    @Epic("Đăng nhập hệ thống")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dn05_emptyPhone() {
        driver.get(baseUrl + "smember");
        loginPage.enterUsername("");
        loginPage.enterPassword("abc123");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                loginPage.pageBodyContains("Không được bỏ trống")
                        || loginPage.waitForToastContaining("Số điện thoại không được bỏ trống"),
                "Kỳ vọng lỗi trống SĐT.");
    }

    @Test(description = "DN6 — Bỏ trống mật khẩu")
    @Epic("Đăng nhập hệ thống")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dn06_emptyPassword() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn.phone trong properties.");
        }
        driver.get(baseUrl + "smember");
        loginPage.enterUsername(TestCredentials.phone());
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                loginPage.pageBodyContains("Không được bỏ trống")
                        || loginPage.waitForToastContaining("Mật khẩu không được bỏ trống"),
                "Kỳ vọng lỗi trống mật khẩu.");
    }
}
