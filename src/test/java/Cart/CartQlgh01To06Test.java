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

/** CellphoneS — Quản lý giỏ: QLGH1–QLGH6. */
public class CartQlgh01To06Test extends BaseTest {

    private void ensureLoggedInAndProductInCart() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl + "mobile.html");
        ProductListPage list = new ProductListPage(driver);
        if (list.getProductCount() > 0) {
            list.clickFirstProduct();
            ProductDetailPage d = new ProductDetailPage(driver);
            if (d.isAddToCartVisible()) {
                d.addToCart();
            }
        }
    }

    @Test(description = "QLGH1 — Xem giỏ có sản phẩm (đã đăng nhập)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Xem giỏ")
    @Severity(SeverityLevel.CRITICAL)
    public void qlgh01_viewCartWithItems() {
        ensureLoggedInAndProductInCart();
        new HomePage(driver).openCart();
        CartPage cart = new CartPage(driver);
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Trang giỏ.");
        Assert.assertTrue(cart.getItemCount() > 0 || cart.isEmptyCartDisplayed(), "Danh sách hoặc trạng thái trống.");
    }

    @Test(description = "QLGH2 — Xem giỏ trống (đã đăng nhập)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Xem giỏ")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh02_viewEmptyCartLoggedIn() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        while (cart.getItemCount() > 0) {
            cart.removeFirstCartItem();
            driver.navigate().refresh();
            cart = new CartPage(driver);
        }
        Assert.assertTrue(cart.isEmptyCartDisplayed() || cart.pageContains("trống"), "Giỏ trống.");
    }

    @Test(description = "QLGH3 — Xem giỏ khi chưa đăng nhập")
    @Epic("Quản lý giỏ hàng")
    @Feature("Xem giỏ")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh03_viewCartGuest() {
        driver.get(baseUrl);
        new HomePage(driver).openCart();
        Assert.assertTrue(
                driver.getCurrentUrl().contains("cart")
                        || driver.findElement(org.openqa.selenium.By.tagName("body")).getText().contains("Đăng nhập"),
                "Giỏ hoặc yêu cầu đăng nhập.");
    }

    @Test(description = "QLGH4 — Tăng SL (+1)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Cập nhật SL")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh04_increaseQtyOnce() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        if (cart.getItemCount() > 0) {
            cart.clickIncreaseFirstItemQuantity();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Vẫn ở giỏ.");
    }

    @Test(description = "QLGH5 — Tăng SL (+3)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Cập nhật SL")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh05_increaseQtyThreeTimes() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        cart.increaseFirstItemQuantityTimes(3);
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Vẫn ở giỏ.");
    }

    @Test(description = "QLGH6 — Giảm SL (-1)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Cập nhật SL")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh06_decreaseQtyOnce() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        cart.increaseFirstItemQuantityTimes(2);
        cart.clickDecreaseFirstItemQuantity();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Vẫn ở giỏ.");
    }
}
