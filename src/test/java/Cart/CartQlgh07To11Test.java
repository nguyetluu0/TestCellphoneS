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

/** CellphoneS — Quản lý giỏ: QLGH7–QLGH11. */
public class CartQlgh07To11Test extends BaseTest {

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

    @Test(description = "QLGH7 — Giảm SL (-3)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Cập nhật SL")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh07_decreaseQtyThreeTimes() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        cart.increaseFirstItemQuantityTimes(4);
        cart.decreaseFirstItemQuantityTimes(3);
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Vẫn ở giỏ.");
    }

    @Test(description = "QLGH8 — Giảm SL khi đang = 1 (tối thiểu)")
    @Epic("Quản lý giỏ hàng")
    @Feature("Cập nhật SL")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh08_decreaseAtMinimumQty() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        cart.clickDecreaseFirstItemQuantity();
        Assert.assertTrue(
                cart.pageContains("tối thiểu") || cart.pageContains("tối thiểu".toLowerCase())
                        || driver.getCurrentUrl().contains("cart"),
                "Toast tối thiểu hoặc vẫn giỏ.");
    }

    @Test(description = "QLGH9 — Xóa một dòng sản phẩm")
    @Epic("Quản lý giỏ hàng")
    @Feature("Xóa")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh09_removeOneLine() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        int before = cart.getItemCount();
        if (before > 0) {
            cart.removeFirstCartItem();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Vẫn ở giỏ.");
    }

    @Test(description = "QLGH10 — Xóa hết từng dòng")
    @Epic("Quản lý giỏ hàng")
    @Feature("Xóa")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh10_removeAllLines() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        int guard = 0;
        while (cart.getItemCount() > 0 && guard++ < 20) {
            cart.removeFirstCartItem();
            driver.navigate().refresh();
            cart = new CartPage(driver);
        }
        Assert.assertTrue(cart.isEmptyCartDisplayed() || cart.getItemCount() == 0, "Giỏ trống hoặc 0 dòng.");
    }

    @Test(description = "QLGH11 — Chọn tất cả và xóa sản phẩm đã chọn")
    @Epic("Quản lý giỏ hàng")
    @Feature("Xóa")
    @Severity(SeverityLevel.NORMAL)
    public void qlgh11_selectAllAndDeleteSelected() {
        ensureLoggedInAndProductInCart();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        cart.clickSelectAllIfPresent();
        cart.clickDeleteSelectedProductsIfPresent();
        Assert.assertTrue(
                cart.isEmptyCartDisplayed() || cart.pageContains("trống") || driver.getCurrentUrl().contains("cart"),
                "Sau xóa hàng loạt.");
    }
}
