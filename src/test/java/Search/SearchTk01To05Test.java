package Search;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.HomePageBaseTest;

/** CellphoneS — Tìm kiếm: TK1–TK5. */
public class SearchTk01To05Test extends HomePageBaseTest {

    @Test(description = "TK1 — Tìm kiếm từ khóa hợp lệ (iphone 13)")
    @Epic("Tìm kiếm sản phẩm")
    @Feature("Từ khóa")
    @Severity(SeverityLevel.NORMAL)
    public void tk01_searchValidKeywordLower() {
        driver.get(baseUrl);
        HomePage home = new HomePage(driver);
        home.searchAndSubmitByEnter("iphone 13");
        String url = driver.getCurrentUrl().toLowerCase();
        String body = driver.findElement(By.tagName("body")).getText().toLowerCase();
        Assert.assertTrue(
                url.contains("tim-kiem") || url.contains("search") || body.contains("iphone"),
                "Kỳ vọng trang kết quả hoặc nội dung liên quan.");
    }

    @Test(description = "TK2 — Tìm kiếm không phân biệt hoa thường (IPHONE 13)")
    @Epic("Tìm kiếm sản phẩm")
    @Feature("Từ khóa")
    @Severity(SeverityLevel.NORMAL)
    public void tk02_searchCaseInsensitive() {
        driver.get(baseUrl);
        HomePage home = new HomePage(driver);
        home.searchAndSubmitByEnter("IPHONE 13");
        String body = driver.findElement(By.tagName("body")).getText().toLowerCase();
        Assert.assertTrue(body.contains("iphone") || driver.getCurrentUrl().toLowerCase().contains("tim-kiem"),
                "Kỳ vọng kết quả liên quan iphone.");
    }

    @Test(description = "TK3 — Tìm kiếm liên tiếp nhiều từ khóa")
    @Epic("Tìm kiếm sản phẩm")
    @Feature("Từ khóa")
    @Severity(SeverityLevel.NORMAL)
    public void tk03_searchConsecutiveKeywords() {
        driver.get(baseUrl);
        HomePage home = new HomePage(driver);
        home.searchAndSubmitByEnter("iphone");
        String u1 = driver.getCurrentUrl().toLowerCase();
        driver.get(baseUrl);
        home = new HomePage(driver);
        home.searchAndSubmitByEnter("samsung");
        String u2 = driver.getCurrentUrl().toLowerCase();
        Assert.assertTrue(
                (u1.contains("tim-kiem") || u1.contains("search") || u1.contains("iphone"))
                        && (u2.contains("tim-kiem") || u2.contains("search") || u2.contains("samsung")),
                "Kỳ vọng hai lần tìm đều cho kết quả hợp lệ.");
    }

    @Test(description = "TK4 — Từ khóa không tồn tại")
    @Epic("Tìm kiếm sản phẩm")
    @Feature("Từ khóa")
    @Severity(SeverityLevel.NORMAL)
    public void tk04_searchNoResults() {
        driver.get(baseUrl);
        HomePage home = new HomePage(driver);
        home.searchAndSubmitByEnter("abcdxyz123");
        boolean noResultMsg = driver.findElement(By.tagName("body")).getText().contains("Không có kết quả")
                || driver.findElement(By.tagName("body")).getText().contains("không có kết quả");
        Assert.assertTrue(
                noResultMsg || driver.getCurrentUrl().toLowerCase().contains("tim-kiem"),
                "Kỳ vọng thông báo không có kết quả hoặc trang tìm kiếm.");
    }

    @Test(description = "TK5 — Ô tìm kiếm trống + Enter")
    @Epic("Tìm kiếm sản phẩm")
    @Feature("Từ khóa")
    @Severity(SeverityLevel.NORMAL)
    public void tk05_searchEmptyKeyword() {
        driver.get(baseUrl);
        HomePage home = new HomePage(driver);
        home.searchAndSubmitByEnter("");
        Assert.assertTrue(
                driver.getCurrentUrl().contains("cellphones.com.vn"),
                "Trang vẫn tải, không crash.");
    }
}
