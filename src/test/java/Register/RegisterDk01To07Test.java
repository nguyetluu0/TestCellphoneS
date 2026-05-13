package Register;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;
import utils.BaseTest;

/**
 * CellphoneS — Đăng ký: case DK1–DK7 (theo bảng test DATN).
 */
public class RegisterDk01To07Test extends BaseTest {
    private RegisterPage registerPage;

    @BeforeMethod
    public void initPage() {
        registerPage = new RegisterPage(driver);
    }

    private void openRegisterUrl() {
        driver.get(baseUrl + "smember/register");
        registerPage.waitForRegisterFormReady();
    }

    @Test(description = "DK1 — Điều hướng đăng ký từ trang chủ")
    @Epic("Đăng ký tài khoản")
    @Feature("Điều hướng")
    @Severity(SeverityLevel.CRITICAL)
    public void dk01_navigateRegisterFromHome() {
        driver.get(baseUrl);
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.openLoginDialog();
        loginPage.clickRegisterTabInModal();
        Assert.assertTrue(registerPage.isSmemberRegisterFormDisplayed(),
                "Form đăng ký Smember phải hiển thị đủ các trường.");
    }

    @Test(description = "DK2 — Điều hướng đăng ký từ popup đăng nhập / Đăng ký ngay")
    @Epic("Đăng ký tài khoản")
    @Feature("Điều hướng")
    @Severity(SeverityLevel.CRITICAL)
    public void dk02_navigateRegisterFromLoginPopup() {
        driver.get(baseUrl);
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.openLoginDialog();
        loginPage.clickRegisterTabInModal();
        if (!registerPage.isSmemberRegisterFormDisplayed()) {
            loginPage.clickRegisterNow();
        }
        Assert.assertTrue(registerPage.isSmemberRegisterFormDisplayed(),
                "Form đăng ký Smember phải hiển thị đủ các trường.");
    }

    @Test(description = "DK3 — Đăng ký thành công (đủ trường hợp lệ) → OTP")
    @Epic("Đăng ký tài khoản")
    @Feature("Thành công")
    @Severity(SeverityLevel.BLOCKER)
    public void dk03_registerSuccessAllValid() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456119",
                "testing10@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.isOtpConfirmationScreen(), "Kỳ vọng màn OTP.");
    }

    @Test(description = "DK4 — Đăng ký thành công (email trống) → OTP")
    @Epic("Đăng ký tài khoản")
    @Feature("Thành công")
    @Severity(SeverityLevel.CRITICAL)
    public void dk04_registerSuccessEmptyEmail() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456120",
                "",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.isOtpConfirmationScreen(), "Kỳ vọng màn OTP khi email trống.");
    }

    @Test(description = "DK5 — Bỏ trống họ tên")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk05_emptyFullName() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "",
                "01/01/1990",
                "0997456121",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        Assert.assertTrue(registerPage.getNameValue().isBlank(), "Họ tên phải trống.");
        registerPage.clickCompleteRegister();
        Assert.assertFalse(registerPage.appearsOtpWithinSeconds(18), "Không được sang OTP.");
        Assert.assertTrue(
                registerPage.waitForToastContaining("Họ tên không được để trống")
                        || registerPage.hasInlineOrVisibleMessage("Họ tên")
                        || !registerPage.appearsOtpWithinSeconds(12),
                "Kỳ vọng toast / lỗi inline họ tên hoặc không sang OTP.");
    }

    @Test(description = "DK6 — Bỏ trống ngày sinh")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk06_emptyBirthDate() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "",
                "0997456121",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.hasInlineOrVisibleMessage("Ngày sinh không được để trống")
                        || registerPage.waitForToastContaining("Ngày sinh không được để trống"),
                "Kỳ vọng lỗi ngày sinh trống.");
    }

    @Test(description = "DK7 — Bỏ trống số điện thoại")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk07_emptyPhone() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.hasInlineOrVisibleMessage("Số điện thoại không được để trống")
                        || registerPage.waitForToastContaining("Số điện thoại không được để trống"),
                "Kỳ vọng lỗi SĐT trống.");
    }
}
