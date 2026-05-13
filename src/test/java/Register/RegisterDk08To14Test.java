package Register;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.RegisterPage;
import utils.BaseTest;
import utils.DatnDates;

/**
 * CellphoneS — Đăng ký: case DK8–DK14.
 */
public class RegisterDk08To14Test extends BaseTest {
    private RegisterPage registerPage;

    @BeforeMethod
    public void initPage() {
        registerPage = new RegisterPage(driver);
    }

    private void openRegisterUrl() {
        driver.get(baseUrl + "smember/register");
        registerPage.waitForRegisterFormReady();
    }

    @Test(description = "DK8 — Bỏ trống mật khẩu")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk08_emptyPassword() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456121",
                "test@gmail.com",
                "",
                ""
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.waitForToastContaining("Mật khẩu không được để trống"),
                "Toast mật khẩu trống.");
    }

    @Test(description = "DK9 — Bỏ trống xác nhận mật khẩu")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk09_emptyConfirmPassword() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456121",
                "test@gmail.com",
                "abcd1234",
                ""
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.waitForToastContaining("Nhập lại mật khẩu không được để trống"),
                "Toast xác nhận mật khẩu trống.");
    }

    @Test(description = "DK10 — Định dạng ngày sai")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk10_invalidDateFormat() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "32/13/1990",
                "0997456121",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.hasInlineOrVisibleMessage("Định dạng ngày không hợp lệ")
                        || registerPage.waitForToastContaining("Định dạng ngày không hợp lệ"),
                "Kỳ vọng báo định dạng ngày không hợp lệ.");
    }

    @Test(description = "DK11 — Chưa đủ 18 tuổi (01/01/2016)")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk11_under18YearsOld() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/2016",
                "0997456121",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertFalse(registerPage.isOtpConfirmationScreen(), "Không được sang OTP.");
        Assert.assertTrue(
                registerPage.waitForToastContaining("Năm sinh không hợp lệ")
                        || registerPage.waitForToastContaining("Đăng ký thất bại"),
                "Kỳ vọng toast lỗi tuổi / đăng ký thất bại.");
    }

    @Test(description = "DK12 — Quá 81 tuổi (01/01/1944)")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dk12_over81YearsOld() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1944",
                "0997456121",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertFalse(registerPage.isOtpConfirmationScreen(), "Không được sang OTP.");
        Assert.assertTrue(
                registerPage.waitForToastContaining("Năm sinh không hợp lệ")
                        || registerPage.waitForToastContaining("không hợp lệ")
                        || registerPage.waitForToastContaining("Đăng ký thất bại"),
                "Kỳ vọng thông báo lỗi năm sinh.");
    }

    @Test(description = "DK13 — Đúng 18 tuổi (theo ngày hiện tại) → OTP")
    @Epic("Đăng ký tài khoản")
    @Feature("Biên tuổi")
    @Severity(SeverityLevel.CRITICAL)
    public void dk13_exactly18YearsOld() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                DatnDates.minusYearsAsDdMmYyyy(18),
                "0997456122",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.isOtpConfirmationScreen(), "18 tuổi kỳ vọng sang OTP.");
    }

    @Test(description = "DK14 — Đúng 81 tuổi (theo ngày hiện tại) → OTP")
    @Epic("Đăng ký tài khoản")
    @Feature("Biên tuổi")
    @Severity(SeverityLevel.CRITICAL)
    public void dk14_exactly81YearsOld() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                DatnDates.minusYearsAsDdMmYyyy(81),
                "0997456123",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.isOtpConfirmationScreen(), "81 tuổi kỳ vọng sang OTP.");
    }
}
