package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By authDialog = By.xpath("//div[@role='dialog' or contains(@class,'modal__content') or contains(@class,'ant-modal')]");
    private final By registerTabInModal = By.xpath(
            "//div[@role='dialog' or contains(@class,'modal') or contains(@class,'ant-modal')]//*[self::button or self::a or self::span]" +
                    "[normalize-space()='Đăng ký' or (contains(normalize-space(.),'Đăng ký') and not(contains(normalize-space(.),'Đăng ký ngay')))]"
    );
    private final By loginTabInModal = By.xpath(
            "//div[@role='dialog' or contains(@class,'modal') or contains(@class,'ant-modal')]//*[self::button or self::a or self::span]" +
                    "[normalize-space()='Đăng nhập' and not(contains(normalize-space(.),'Đăng ký'))]"
    );
    private final By phoneOrEmailInput = By.xpath(
            "(//*[normalize-space()='Số điện thoại']/following::input[1])[1] | " +
            "//input[@name='phone' or @type='tel' or contains(@placeholder,'Số điện thoại')]"
    );
    private final By passwordInput = By.xpath("//input[@type='password']");
    private final By loginButton = By.xpath(
            "//button[contains(normalize-space(.),'Đăng nhập') and not(@disabled)]"
    );
    private final By registerNowLink = By.xpath(
            "//a[contains(@href,'/register') or contains(normalize-space(.),'Đăng ký ngay')] | " +
                    "//button[contains(normalize-space(.),'Đăng ký ngay')]"
    );
    private final By errorMessage = By.xpath(
            "//*[contains(@class,'error') or contains(@class,'alert') or contains(text(),'không đúng') or contains(text(),'không hợp lệ')]"
    );

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Step("Chờ popup đăng nhập hiển thị")
    public void waitForAuthDialog() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(authDialog));
        } catch (TimeoutException ex) {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(phoneOrEmailInput),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']"))
            ));
        }
    }

    @Step("Chọn tab Đăng nhập trong popup (nếu có)")
    public void clickLoginTabInModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginTabInModal)).click();
        } catch (TimeoutException ignored) {
        }
    }

    @Step("Chọn Đăng ký trong popup (tab / liên kết, không phải Đăng ký ngay)")
    public void clickRegisterTabInModal() {
        wait.until(ExpectedConditions.elementToBeClickable(registerTabInModal)).click();
    }

    @Step("Nhập email hoặc số điện thoại")
    public void enterUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneOrEmailInput));
        input.clear();
        input.sendKeys(username);
    }

    @Step("Nhập mật khẩu")
    public void enterPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        input.clear();
        input.sendKeys(password);
    }

    @Step("Nhấn nút đăng nhập")
    public HomePage clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return new HomePage(driver);
    }

    public void clickRegisterNow() {
        wait.until(ExpectedConditions.elementToBeClickable(registerNowLink)).click();
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (TimeoutException ex) {
            return "";
        }
    }

    public boolean isLoginFormVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(phoneOrEmailInput)).isDisplayed()
                    && wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).isDisplayed();
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public boolean pageBodyContains(String fragment) {
        try {
            String body = driver.findElement(By.tagName("body")).getText();
            return body != null && body.contains(fragment);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForToastContaining(String fragment) {
        long end = System.currentTimeMillis() + 12000;
        while (System.currentTimeMillis() < end) {
            if (pageBodyContains(fragment)) {
                return true;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return pageBodyContains(fragment);
            }
        }
        return pageBodyContains(fragment);
    }

    public boolean isLoginSubmitButtonDisplayed() {
        try {
            return driver.findElement(loginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
