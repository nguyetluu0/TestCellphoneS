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

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItem = By.cssSelector(".product-item, .cart-item, .cart__product, [class*='cart-item']");
    private final By emptyCartText = By.xpath(
            "//*[contains(text(),'Giỏ hàng của bạn đang trống') or contains(text(),'Giỏ hàng trống')]"
    );
    private final By checkoutButton = By.xpath("//button[contains(.,'Tiến hành đặt hàng') or contains(.,'Thanh toán')]");
    private final By quantityPlus = By.xpath(
            "//button[contains(@class,'plus') or contains(@aria-label,'Tăng') or normalize-space()='+'] | " +
                    "//*[contains(@class,'qty')]//button[last()]"
    );
    private final By quantityMinus = By.xpath(
            "//button[contains(@class,'minus') or contains(@class,'sub') or contains(@aria-label,'Giảm')] | " +
                    "//*[contains(@class,'qty')]//button[1]"
    );
    private final By removeItemButton = By.xpath(
            "//button[contains(.,'Xóa')] | //a[contains(.,'Xóa')] | //*[contains(@class,'remove')]"
    );

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public int getItemCount() {
        List<WebElement> items = driver.findElements(cartItem);
        return items.size();
    }

    public boolean isEmptyCartDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartText)).isDisplayed();
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public void clickCheckout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        } catch (TimeoutException ex) {
            driver.get("https://cellphones.com.vn/cart");
        }
    }

    @Step("Tăng số lượng sản phẩm đầu tiên trong giỏ")
    public void clickIncreaseFirstItemQuantity() {
        List<WebElement> buttons = driver.findElements(quantityPlus);
        if (!buttons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(buttons.get(0))).click();
        }
    }

    @Step("Giảm số lượng sản phẩm đầu tiên trong giỏ")
    public void clickDecreaseFirstItemQuantity() {
        List<WebElement> buttons = driver.findElements(quantityMinus);
        if (!buttons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(buttons.get(0))).click();
        }
    }

    @Step("Xóa sản phẩm đầu tiên khỏi giỏ")
    public void removeFirstCartItem() {
        List<WebElement> removes = driver.findElements(removeItemButton);
        if (!removes.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(removes.get(0))).click();
        }
    }

    public void increaseFirstItemQuantityTimes(int times) {
        for (int i = 0; i < times; i++) {
            clickIncreaseFirstItemQuantity();
        }
    }

    public void decreaseFirstItemQuantityTimes(int times) {
        for (int i = 0; i < times; i++) {
            clickDecreaseFirstItemQuantity();
        }
    }

    public void clickSelectAllIfPresent() {
        List<WebElement> boxes = driver.findElements(By.xpath(
                "//input[@type='checkbox' and (contains(@aria-label,'Chọn tất') or contains(@title,'Chọn tất') or following-sibling::*[contains(.,'Chọn tất cả')])] | " +
                        "//label[contains(.,'Chọn tất cả')]//input[@type='checkbox']"
        ));
        if (!boxes.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(boxes.get(0))).click();
        }
    }

    public void clickDeleteSelectedProductsIfPresent() {
        List<WebElement> btns = driver.findElements(By.xpath(
                "//button[contains(.,'Xóa sản phẩm đã chọn')] | //a[contains(.,'Xóa sản phẩm đã chọn')]"
        ));
        if (!btns.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(btns.get(0))).click();
        }
    }

    public boolean pageContains(String fragment) {
        try {
            return driver.findElement(By.tagName("body")).getText().contains(fragment);
        } catch (Exception e) {
            return false;
        }
    }
}
