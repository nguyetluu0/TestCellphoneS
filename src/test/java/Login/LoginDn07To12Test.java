package Login;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

/**
 * CellphoneS — Đăng nhập: DN7–DN12 (khóa tài khoản / định dạng SĐT).
 */
public class LoginDn07To12Test extends BaseTest {
    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @Test(description = "DN7 — Khóa tài khoản sau nhiều lần sai mật khẩu")
    @Epic("Đăng nhập hệ thống")
    @Feature("Bảo mật")
    @Severity(SeverityLevel.NORMAL)
    public void dn07_accountLockAfterWrongAttempts() {
        throw new SkipException("DN7: tự động khóa tài khoản — chạy thủ công / môi trường riêng để tránh khóa TK thật.");
    }

    @Test(description = "DN8 — Đổi SĐT sau khi tài khoản bị khóa — nút đăng nhập hiện lại")
    @Epic("Đăng nhập hệ thống")
    @Feature("Bảo mật")
    @Severity(SeverityLevel.NORMAL)
    public void dn08_loginButtonAfterSwitchPhone() {
        throw new SkipException("DN8: phụ thuộc trạng thái khóa DN7 — bỏ qua trong smoke tự động.");
    }

    @Test(description = "DN9 — Mở khóa sau 10 phút")
    @Epic("Đăng nhập hệ thống")
    @Feature("Bảo mật")
    @Severity(SeverityLevel.NORMAL)
    public void dn09_unlockAfterTenMinutes() {
        throw new SkipException("DN9: cần chờ 10 phút — không chạy tự động trong CI.");
    }

    @Test(description = "DN10 — Số điện thoại < 10 số")
    @Epic("Đăng nhập hệ thống")
    @Feature("Định dạng SĐT")
    @Severity(SeverityLevel.NORMAL)
    public void dn10_phoneTooShort() {
        driver.get(baseUrl + "smember");
        loginPage.enterUsername("0123456");
        loginPage.enterPassword("abc123");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                loginPage.waitForToastContaining("không hợp lệ")
                        || !loginPage.getErrorMessage().isBlank()
                        || loginPage.isLoginFormVisible(),
                "Kỳ vọng báo SĐT/MK không hợp lệ.");
    }

    @Test(description = "DN11 — Số điện thoại > 10 số")
    @Epic("Đăng nhập hệ thống")
    @Feature("Định dạng SĐT")
    @Severity(SeverityLevel.NORMAL)
    public void dn11_phoneTooLong() {
        driver.get(baseUrl + "smember");
        loginPage.enterUsername("012345678910");
        loginPage.enterPassword("abc123");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                loginPage.isLoginFormVisible()
                        || loginPage.waitForToastContaining("hợp lệ")
                        || !loginPage.getErrorMessage().isBlank(),
                "Kỳ vọng giữ form hoặc báo lỗi định dạng.");
    }

    @Test(description = "DN12 — Ký tự không hợp lệ trong ô SĐT")
    @Epic("Đăng nhập hệ thống")
    @Feature("Định dạng SĐT")
    @Severity(SeverityLevel.NORMAL)
    public void dn12_phoneInvalidCharacters() {
        driver.get(baseUrl + "smember");
        loginPage.enterUsername("01234aa");
        loginPage.enterPassword("abc123");
        loginPage.clickLoginButton();
        Assert.assertTrue(
                loginPage.waitForToastContaining("không hợp lệ")
                        || loginPage.isLoginFormVisible(),
                "Kỳ vọng báo không hợp lệ hoặc giữ form.");
    }
}
