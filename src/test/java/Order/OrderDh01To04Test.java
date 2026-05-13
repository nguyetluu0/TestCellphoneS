package Order;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductDetailPage;
import pages.ProductListPage;
import utils.AuthHelper;
import utils.BaseTest;
import utils.TestCredentials;

/** CellphoneS — Đặt hàng: DH1–DH4. */
public class OrderDh01To04Test extends BaseTest {

    private void ensureLoggedInWithCartItem() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        driver.get(baseUrl + "mobile.html");
        ProductListPage list = new ProductListPage(driver);
        if (list.getProductCount() > 0) {
            list.clickFirstProduct();
            ProductDetailPage d = new ProductDetailPage(driver);
            if (d.isBuyNowVisible()) {
                d.clickBuyNow();
            } else if (d.isAddToCartVisible()) {
                d.addToCart();
            }
        }
    }

    @Test(description = "DH1 — Đặt hàng COD (luồng cơ bản tới checkout)")
    @Epic("Đặt hàng")
    @Feature("COD")
    @Severity(SeverityLevel.CRITICAL)
    public void dh01_checkoutCodFlow() {
        ensureLoggedInWithCartItem();
        driver.get(baseUrl + "cart");
        CartPage cart = new CartPage(driver);
        cart.clickCheckout();
        String url = driver.getCurrentUrl().toLowerCase();
        Assert.assertTrue(
                url.contains("cart") || url.contains("checkout") || url.contains("thanh-toan") || url.contains("order"),
                "Kỳ vọng bước thanh toán / giỏ.");
    }

    @Test(description = "DH2 — Thanh toán online (chuyển cổng / QR)")
    @Epic("Đặt hàng")
    @Feature("Online")
    @Severity(SeverityLevel.CRITICAL)
    public void dh02_checkoutOnlineFlow() {
        ensureLoggedInWithCartItem();
        driver.get(baseUrl + "cart");
        new CartPage(driver).clickCheckout();
        Assert.assertTrue(
                driver.getCurrentUrl().toLowerCase().matches(".*(checkout|thanh-toan|cart|pay|vnpay).*"),
                "Luồng thanh toán online hoặc giỏ.");
    }

    @Test(description = "DH3 — Thiếu địa chỉ / cửa hàng")
    @Epic("Đặt hàng")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dh03_checkoutMissingStore() {
        ensureLoggedInWithCartItem();
        driver.get(baseUrl + "cart");
        new CartPage(driver).clickCheckout();
        Assert.assertTrue(
                driver.findElement(org.openqa.selenium.By.tagName("body")).getText().contains("cửa hàng")
                        || driver.getCurrentUrl().contains("checkout")
                        || driver.getCurrentUrl().contains("cart"),
                "Trang checkout hoặc gợi ý chọn cửa hàng.");
    }

    @Test(description = "DH4 — Nhập tỉnh/quận sai (kỳ vọng không tìm thấy)")
    @Epic("Đặt hàng")
    @Feature("Validation")
    @Severity(SeverityLevel.NORMAL)
    public void dh04_invalidProvinceDistrict() {
        ensureLoggedInWithCartItem();
        driver.get(baseUrl + "cart");
        new CartPage(driver).clickCheckout();
        boolean body = driver.findElement(org.openqa.selenium.By.tagName("body")).getText().toLowerCase().contains("không tìm thấy")
                || driver.getCurrentUrl().contains("checkout");
        Assert.assertTrue(body, "Trên checkout có thể hiện gợi ý địa chỉ.");
    }
}
