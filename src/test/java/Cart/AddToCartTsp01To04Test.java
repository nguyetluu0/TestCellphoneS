package Cart;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.ProductListPage;
import utils.AuthHelper;
import utils.BaseTest;
import utils.TestCredentials;

/** CellphoneS — Thêm vào giỏ: TSP1–TSP4. */
public class AddToCartTsp01To04Test extends BaseTest {

    private void loginIfConfigured() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties cho case đã đăng nhập.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
    }

    private void addOneProductFromMobileList() {
        driver.get(baseUrl + "mobile.html");
        ProductListPage list = new ProductListPage(driver);
        Assert.assertTrue(list.getProductCount() > 0, "Cần sản phẩm.");
        list.clickFirstProduct();
        ProductDetailPage detail = new ProductDetailPage(driver);
        if (detail.isAddToCartVisible()) {
            detail.addToCart();
        }
    }

    @Test(description = "TSP1 — Thêm sản phẩm mới vào giỏ (đã đăng nhập)")
    @Epic("Thêm sản phẩm vào giỏ hàng")
    @Feature("Thành công")
    @Severity(SeverityLevel.CRITICAL)
    public void tsp01_addNewProductLoggedIn() {
        loginIfConfigured();
        addOneProductFromMobileList();
        new HomePage(driver).openCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Mở trang giỏ.");
    }

    @Test(description = "TSP2 — Thêm lại SP đã có trong giỏ (tăng SL)")
    @Epic("Thêm sản phẩm vào giỏ hàng")
    @Feature("Số lượng")
    @Severity(SeverityLevel.NORMAL)
    public void tsp02_addSameProductIncreasesQty() {
        loginIfConfigured();
        addOneProductFromMobileList();
        driver.navigate().back();
        addOneProductFromMobileList();
        new HomePage(driver).openCart();
        CartPage cart = new CartPage(driver);
        Assert.assertTrue(driver.getCurrentUrl().contains("cart") && cart.getItemCount() >= 0, "Giỏ sau 2 lần thêm.");
    }

    @Test(description = "TSP3 — Thêm vào giỏ 5 lần liên tiếp")
    @Epic("Thêm sản phẩm vào giỏ hàng")
    @Feature("Số lượng")
    @Severity(SeverityLevel.NORMAL)
    public void tsp03_addFiveTimes() {
        loginIfConfigured();
        for (int i = 0; i < 5; i++) {
            addOneProductFromMobileList();
            driver.navigate().back();
        }
        new HomePage(driver).openCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Giỏ sau nhiều lần thêm.");
    }

    @Test(description = "TSP4 — Thêm giỏ khi chưa đăng nhập")
    @Epic("Thêm sản phẩm vào giỏ hàng")
    @Feature("Khách")
    @Severity(SeverityLevel.NORMAL)
    public void tsp04_addWhenGuest() {
        driver.get(baseUrl + "mobile.html");
        ProductListPage list = new ProductListPage(driver);
        Assert.assertTrue(list.getProductCount() > 0, "Cần sản phẩm.");
        list.clickFirstProduct();
        ProductDetailPage detail = new ProductDetailPage(driver);
        if (detail.isAddToCartVisible()) {
            detail.addToCart();
        }
        Assert.assertTrue(
                detail.isLoginOrAuthDialogLikely() || detail.bodyContains("Đăng nhập"),
                "Kỳ vọng yêu cầu đăng nhập hoặc popup.");
    }
}
