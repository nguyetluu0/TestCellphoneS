package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchInput = By.cssSelector(
            "input[placeholder='Bạn muốn mua gì hôm nay?'], " +
            "header input[type='text'], " +
            "div[class*='header'] input[type='text']"
    );
    private final By searchSuggestions = By.cssSelector(
            "div[class*='suggest'] a, div[class*='search'] a, ul[class*='suggest'] a"
    );
    private final By loginBtn = By.xpath(
            "//a[contains(@href,'/smember') or contains(@href,'/login') or contains(normalize-space(.),'Đăng nhập')]"
    );
    private final By userMenu = By.xpath(
            "//*[contains(@class,'header__login') and not(contains(.,'Đăng nhập'))] | " +
            "//*[contains(@class,'login-name')] | " +
            "//a[contains(@href,'/smember/orders')]"
    );
    private final By cartIcon = By.xpath("//a[contains(@href,'/cart')]");
    private final By orderLookupLink = By.xpath(
            "//*[self::a or self::span or self::button][contains(normalize-space(.),'Tra cứu đơn hàng')]"
    );

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void searchKeyword(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(keyword);
    }

    @Step("Tìm kiếm và nhấn Enter")
    public void searchAndSubmitByEnter(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(keyword);
        input.sendKeys(Keys.ENTER);
    }

    public String getSearchInputValue() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return input.getAttribute("value");
    }

    public List<String> getSuggestionTitles() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchSuggestions));
        return driver.findElements(searchSuggestions).stream()
                .map(WebElement::getText)
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());
    }

    public boolean containsKeywordInSuggestions(String keyword) {
        String expected = keyword.toLowerCase();
        return getSuggestionTitles().stream().allMatch(s -> s.toLowerCase().contains(expected));
    }

    public boolean isUserLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(userMenu)).isDisplayed();
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }

    @Step("Mở popup đăng nhập / Smember từ trang chủ")
    public LoginPage openLoginDialog() {
        clickLogin();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForAuthDialog();
        return loginPage;
    }

    public void openCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        } catch (TimeoutException ex) {
            driver.get("https://cellphones.com.vn/cart");
        }
    }

    @Step("Mở Tra cứu đơn hàng từ header/menu")
    public void openOrderLookupFromHeader() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(orderLookupLink)).click();
        } catch (TimeoutException ex) {
            driver.get("https://cellphones.com.vn/smember/orders");
        }
    }
}
