package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By fullNameInput = By.xpath("//input[@name='name' or @name='fullName']");
    private final By phoneInput = By.xpath("//input[@name='phone' or @type='tel']");
    private final By addressInput = By.xpath("//textarea[@name='address'] | //input[@name='address']");
    private final By codMethod = By.xpath("//*[contains(text(),'Thanh toán khi nhận hàng')]");
    private final By placeOrderButton = By.xpath("//button[contains(.,'Đặt hàng') or contains(.,'Hoàn tất')]");
    private final By successText = By.xpath("//*[contains(text(),'đặt hàng thành công') or contains(text(),'Cảm ơn bạn')]");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void fillShippingInfo(String fullName, String phone, String address) {
        WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput));
        name.clear();
        name.sendKeys(fullName);

        WebElement phoneElement = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        phoneElement.clear();
        phoneElement.sendKeys(phone);

        WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput));
        addressElement.clear();
        addressElement.sendKeys(address);
    }

    public void selectCashOnDelivery() {
        wait.until(ExpectedConditions.elementToBeClickable(codMethod)).click();
    }

    public void placeOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
    }

    public boolean isOrderSuccessMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successText)).isDisplayed();
    }
}
