package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductDetailPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productTitle = By.cssSelector("h1");
    private final By productPrice = By.cssSelector(
            ".block-box-price, .box-info__box-price, .box-product-price, [class*='price']"
    );
    private final By productImages = By.cssSelector("img[src*='cdn'], .product-gallery img, [class*='gallery'] img");
    private final By addToCartButton = By.xpath(
            "//button[contains(normalize-space(.),'Thêm vào giỏ') or contains(normalize-space(.),'Thêm vào giỏ hàng')]"
    );
    private final By buyNowButton = By.xpath("//button[contains(normalize-space(.),'Mua ngay')]");
    private final By anyPrimaryCta = By.xpath("//button[contains(.,'Thêm vào giỏ') or contains(.,'Mua ngay')]");

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean isProductInfoVisible() {
        boolean hasTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle)).isDisplayed();
        boolean hasPrice = !driver.findElements(productPrice).isEmpty();
        return hasTitle && hasPrice;
    }

    @Step("Thêm vào giỏ (nút riêng nếu có)")
    public void addToCart() {
        List<WebElement> btns = driver.findElements(addToCartButton);
        if (!btns.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(btns.get(0))).click();
            return;
        }
        wait.until(ExpectedConditions.elementToBeClickable(anyPrimaryCta)).click();
    }

    @Step("Mua ngay")
    public void clickBuyNow() {
        wait.until(ExpectedConditions.elementToBeClickable(buyNowButton)).click();
    }

    public boolean isAddToCartVisible() {
        try {
            return !driver.findElements(addToCartButton).isEmpty()
                    && wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton)).isDisplayed();
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public boolean isBuyNowVisible() {
        List<WebElement> els = driver.findElements(buyNowButton);
        if (els.isEmpty()) {
            return false;
        }
        try {
            return els.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasProductImage() {
        return !driver.findElements(productImages).isEmpty();
    }

    public boolean bodyContains(String fragment) {
        String body = driver.findElement(By.tagName("body")).getText();
        return body != null && body.toLowerCase().contains(fragment.toLowerCase());
    }

    public boolean waitForToastContaining(String fragment) {
        long end = System.currentTimeMillis() + 12000;
        while (System.currentTimeMillis() < end) {
            if (bodyContains(fragment)) {
                return true;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return bodyContains(fragment);
            }
        }
        return bodyContains(fragment);
    }

    public boolean isLoginOrAuthDialogLikely() {
        try {
            return wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@role='dialog']//input[@type='password']")),
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@class,'modal') or contains(@class,'drawer')]//input[@type='password']"))
            )) != null;
        } catch (TimeoutException ex) {
            return driver.findElements(By.xpath("//div[@role='dialog']//input[@type='password']")).size() > 0;
        }
    }
}
