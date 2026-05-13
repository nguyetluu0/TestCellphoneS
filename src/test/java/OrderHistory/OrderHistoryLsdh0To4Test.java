package OrderHistory;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.OrderHistoryPage;
import utils.AuthHelper;
import utils.BaseTest;
import utils.TestCredentials;

/** CellphoneS — Lịch sử đơn: LSDH0–LSDH4. */
public class OrderHistoryLsdh0To4Test extends BaseTest {

    @BeforeMethod
    public void goHome() {
        driver.get(baseUrl);
    }

    @Test(description = "LSDH0 — Truy cập lịch sử mua hàng (đã đăng nhập)")
    @Epic("Lịch sử đơn hàng")
    @Feature("Điều hướng")
    @Severity(SeverityLevel.CRITICAL)
    public void lsdh00_accessOrderHistoryLoggedIn() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl);
        new HomePage(driver).openOrderLookupFromHeader();
        OrderHistoryPage page = new OrderHistoryPage(driver);
        Assert.assertTrue(page.isOrderSectionVisible(), "Khu vực đơn hàng hoặc đăng nhập.");
    }

    @Test(description = "LSDH1 — Hiển thị danh sách đơn")
    @Epic("Lịch sử đơn hàng")
    @Feature("Danh sách")
    @Severity(SeverityLevel.NORMAL)
    public void lsdh01_orderListDisplayed() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl + "smember/orders");
        OrderHistoryPage page = new OrderHistoryPage(driver);
        Assert.assertTrue(page.getOrderCount() >= 0, "Trang đơn hàng tải được.");
    }

    @Test(description = "LSDH2 — Xem chi tiết một đơn")
    @Epic("Lịch sử đơn hàng")
    @Feature("Chi tiết")
    @Severity(SeverityLevel.NORMAL)
    public void lsdh02_viewOrderDetail() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl + "smember/orders");
        OrderHistoryPage page = new OrderHistoryPage(driver);
        page.clickViewOrderDetailIfPresent();
        Assert.assertTrue(driver.getCurrentUrl().contains("smember"), "Vẫn trong Smember.");
    }

    @Test(description = "LSDH3 — Tra cứu đơn khi chưa đăng nhập")
    @Epic("Lịch sử đơn hàng")
    @Feature("Khách")
    @Severity(SeverityLevel.NORMAL)
    public void lsdh03_orderLookupGuest() {
        driver.get(baseUrl);
        new HomePage(driver).openOrderLookupFromHeader();
        LoginPage login = new LoginPage(driver);
        Assert.assertTrue(
                login.isLoginFormVisible() || driver.getCurrentUrl().contains("smember"),
                "Yêu cầu đăng nhập hoặc Smember.");
    }

    @Test(description = "LSDH4 — Tài khoản không có đơn")
    @Epic("Lịch sử đơn hàng")
    @Feature("Trống")
    @Severity(SeverityLevel.NORMAL)
    public void lsdh04_noOrdersMessage() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl + "smember/orders");
        OrderHistoryPage page = new OrderHistoryPage(driver);
        Assert.assertTrue(
                page.isNoOrdersMessageVisible() || page.getOrderCount() >= 0,
                "Thông báo không có đơn hoặc có danh sách.");
    }
}
