package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderHistoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By orderRows = By.cssSelector(".order-item, .order__item, .order-list-item, tr.order");
    private final By emptyOrderText = By.xpath("//*[contains(text(),'dang nhap') or contains(text(),'SMEMBER') or contains(text(),'don hang')]");

    public OrderHistoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public int getOrderCount() {
        List<WebElement> orders = driver.findElements(orderRows);
        return orders.size();
    }

    public boolean isOrderSectionVisible() {
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(orderRows),
                ExpectedConditions.visibilityOfElementLocated(emptyOrderText),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']"))
        )) != null;
    }

    public boolean isNoOrdersMessageVisible() {
        return driver.findElements(By.xpath("//*[contains(.,'Không có đơn hàng')]")).stream().anyMatch(WebElement::isDisplayed);
    }

    public void clickViewOrderDetailIfPresent() {
        List<WebElement> links = driver.findElements(By.xpath(
                "//a[contains(.,'Xem chi tiết')] | //button[contains(.,'Xem chi tiết')]"
        ));
        if (!links.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(links.get(0))).click();
        }
    }
}
