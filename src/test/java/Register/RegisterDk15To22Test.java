package Register;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.RegisterPage;
import utils.BaseTest;
import utils.TestCredentials;

/**
 * CellphoneS — Đăng ký: case DK15–DK22.
 */
public class RegisterDk15To22Test extends BaseTest {
    private RegisterPage registerPage;

    @BeforeMethod
    public void initPage() {
        registerPage = new RegisterPage(driver);
    }

    private void openRegisterUrl() {
        driver.get(baseUrl + "smember/register");
        registerPage.waitForRegisterFormReady();
    }

    @Test(description = "DK15 — Số điện thoại < 10 số")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation SĐT")
    @Severity(SeverityLevel.NORMAL)
    public void dk15_phoneTooShort() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "099745612",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.waitForToastContaining("Số điện thoại không hợp lệ"),
                "Toast SĐT không hợp lệ.");
    }

    @Test(description = "DK16 — Số điện thoại > 10 số")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation SĐT")
    @Severity(SeverityLevel.NORMAL)
    public void dk16_phoneTooLong() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "09974561245",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.waitForToastContaining("Số điện thoại không hợp lệ")
                        || !registerPage.getPhoneFieldValue().replaceAll("\\D", "").equals("09974561245"),
                "Kỳ vọng cảnh báo SĐT hoặc input bị giới hạn.");
    }

    @Test(description = "DK17 — Số điện thoại có ký tự không phải số")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation SĐT")
    @Severity(SeverityLevel.NORMAL)
    public void dk17_phoneNonNumeric() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "09974abcde",
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        String phoneValue = registerPage.getPhoneFieldValue();
        Assert.assertFalse(
                phoneValue.matches(".*[A-Za-z].*"),
                "Kỳ vọng không còn chữ trong ô SĐT.");
        Assert.assertTrue(
                registerPage.waitForToastContaining("Số điện thoại không hợp lệ"),
                "Toast SĐT không hợp lệ.");
    }

    @Test(description = "DK18 — Số điện thoại trùng tài khoản đã đăng ký (0967824921)")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation SĐT")
    @Severity(SeverityLevel.CRITICAL)
    public void dk18_duplicateRegisteredPhone() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                TestCredentials.phone().isBlank() ? "0967824921" : TestCredentials.phone(),
                "test@gmail.com",
                "abcd1234",
                "abcd1234"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.waitForToastContaining("Số điện thoại đã tồn tại")
                        || registerPage.waitForToastContaining("đã tồn tại"),
                "Kỳ vọng báo SĐT đã tồn tại.");
    }

    @Test(description = "DK19 — Mật khẩu không có chữ")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation mật khẩu")
    @Severity(SeverityLevel.NORMAL)
    public void dk19_passwordNoLetters() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456125",
                "test@gmail.com",
                "123456",
                "123456"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.hasRedHintForPasswordRule(), "Gợi ý đỏ quy tắc MK.");
        Assert.assertTrue(registerPage.waitForToastContaining("Mật khẩu không hợp lệ"), "Toast MK không hợp lệ.");
    }

    @Test(description = "DK20 — Mật khẩu không có số")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation mật khẩu")
    @Severity(SeverityLevel.NORMAL)
    public void dk20_passwordNoDigits() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456125",
                "test@gmail.com",
                "abcdabcd",
                "abcdabcd"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.hasRedHintForPasswordRule(), "Gợi ý đỏ quy tắc MK.");
        Assert.assertTrue(registerPage.waitForToastContaining("Mật khẩu không hợp lệ"), "Toast MK không hợp lệ.");
    }

    @Test(description = "DK21 — Mật khẩu < 6 ký tự")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation mật khẩu")
    @Severity(SeverityLevel.NORMAL)
    public void dk21_passwordTooShort() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456125",
                "test@gmail.com",
                "abcd1",
                "abcd1"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(registerPage.hasRedHintForPasswordRule(), "Gợi ý đỏ quy tắc MK.");
        Assert.assertTrue(registerPage.waitForToastContaining("Mật khẩu không hợp lệ"), "Toast MK không hợp lệ.");
    }

    @Test(description = "DK22 — Xác nhận mật khẩu không khớp")
    @Epic("Đăng ký tài khoản")
    @Feature("Validation mật khẩu")
    @Severity(SeverityLevel.NORMAL)
    public void dk22_confirmPasswordMismatch() {
        openRegisterUrl();
        registerPage.fillRegisterForm(
                "Nguyễn Văn Đào",
                "01/01/1990",
                "0997456125",
                "test@gmail.com",
                "abcd1234",
                "abcd1235"
        );
        registerPage.clickCompleteRegister();
        Assert.assertTrue(
                registerPage.hasInlineOrVisibleMessage("Mật khẩu không khớp")
                        || registerPage.hasInlineOrVisibleMessage("không khớp"),
                "Lỗi inline không khớp.");
        Assert.assertTrue(
                registerPage.waitForToastContaining("Nhập lại mật khẩu không khớp"),
                "Toast xác nhận không khớp.");
    }
}
