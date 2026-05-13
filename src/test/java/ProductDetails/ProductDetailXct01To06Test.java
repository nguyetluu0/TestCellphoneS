package ProductDetails;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.ProductDetailPage;
import pages.ProductListPage;
import utils.AuthHelper;
import utils.BaseTest;
import utils.TestCredentials;

/** CellphoneS — Chi tiết SP: XCT1–XCT6. */
public class ProductDetailXct01To06Test extends BaseTest {

    private void openFirstProductFromMobileList() {
        driver.get(baseUrl + "mobile.html");
        ProductListPage list = new ProductListPage(driver);
        Assert.assertTrue(list.getProductCount() > 0, "Cần có sản phẩm trên danh sách.");
        list.clickFirstProduct();
    }

    @Test(description = "XCT1 — Điều hướng đúng trang chi tiết")
    @Epic("Xem chi tiết sản phẩm")
    @Feature("Điều hướng")
    @Severity(SeverityLevel.CRITICAL)
    public void xct01_navigateToProductDetail() {
        openFirstProductFromMobileList();
        ProductDetailPage detail = new ProductDetailPage(driver);
        Assert.assertTrue(detail.isProductInfoVisible(), "Trang chi tiết hiển thị thông tin.");
        Assert.assertTrue(driver.getCurrentUrl().contains("cellphones.com.vn"), "URL hợp lệ.");
    }

    @Test(description = "XCT2 — Hiển thị đầy đủ thông tin sản phẩm")
    @Epic("Xem chi tiết sản phẩm")
    @Feature("Nội dung")
    @Severity(SeverityLevel.NORMAL)
    public void xct02_productInformationBlocks() {
        openFirstProductFromMobileList();
        ProductDetailPage detail = new ProductDetailPage(driver);
        Assert.assertTrue(detail.isProductInfoVisible(), "Tên + giá.");
        Assert.assertTrue(
                detail.hasProductImage() || detail.bodyContains("Mô tả") || detail.bodyContains("Thông số"),
                "Kỳ vọng có ảnh hoặc khối mô tả/thông số.");
    }

    @Test(description = "XCT3 — Thêm giỏ khi chưa đăng nhập → popup đăng nhập")
    @Epic("Xem chi tiết sản phẩm")
    @Feature("CTA")
    @Severity(SeverityLevel.NORMAL)
    public void xct03_addToCartGuestShowsLogin() {
        openFirstProductFromMobileList();
        ProductDetailPage detail = new ProductDetailPage(driver);
        if (!detail.isAddToCartVisible()) {
            throw new SkipException("Không có nút Thêm vào giỏ trên SP này.");
        }
        detail.addToCart();
        Assert.assertTrue(
                detail.isLoginOrAuthDialogLikely() || detail.bodyContains("Đăng nhập"),
                "Kỳ vọng yêu cầu đăng nhập.");
    }

    @Test(description = "XCT4 — Mua ngay khi chưa đăng nhập → popup đăng nhập")
    @Epic("Xem chi tiết sản phẩm")
    @Feature("CTA")
    @Severity(SeverityLevel.NORMAL)
    public void xct04_buyNowGuestShowsLogin() {
        openFirstProductFromMobileList();
        ProductDetailPage detail = new ProductDetailPage(driver);
        if (!detail.isBuyNowVisible()) {
            throw new SkipException("Không có nút Mua ngay.");
        }
        detail.clickBuyNow();
        Assert.assertTrue(
                detail.isLoginOrAuthDialogLikely() || detail.bodyContains("Đăng nhập"),
                "Kỳ vọng yêu cầu đăng nhập.");
    }

    @Test(description = "XCT5 — Thêm giỏ khi đã đăng nhập")
    @Epic("Xem chi tiết sản phẩm")
    @Feature("CTA")
    @Severity(SeverityLevel.CRITICAL)
    public void xct05_addToCartWhenLoggedIn() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        openFirstProductFromMobileList();
        ProductDetailPage detail = new ProductDetailPage(driver);
        if (detail.isAddToCartVisible()) {
            detail.addToCart();
        }
        Assert.assertTrue(
                detail.waitForToastContaining("Thành công")
                        || detail.bodyContains("giỏ")
                        || driver.getCurrentUrl().contains("cart"),
                "Kỳ vọng phản hồi thêm giỏ.");
    }

    @Test(description = "XCT6 — Mua ngay khi đã đăng nhập → luồng đặt hàng")
    @Epic("Xem chi tiết sản phẩm")
    @Feature("CTA")
    @Severity(SeverityLevel.CRITICAL)
    public void xct06_buyNowWhenLoggedIn() {
        if (!TestCredentials.isConfigured()) {
            throw new SkipException("Cần datn-test.properties.");
        }
        AuthHelper.loginSmember(driver, baseUrl);
        openFirstProductFromMobileList();
        ProductDetailPage detail = new ProductDetailPage(driver);
        if (!detail.isBuyNowVisible()) {
            throw new SkipException("Không có nút Mua ngay.");
        }
        detail.clickBuyNow();
        String url = driver.getCurrentUrl().toLowerCase();
        Assert.assertTrue(
                url.contains("cart") || url.contains("checkout") || url.contains("thanh-toan") || url.contains("order"),
                "Kỳ vọng điều hướng checkout/giỏ.");
    }
}
