package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductListPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productCards = By.cssSelector("div.product-item, div.product-info-container");

    public ProductListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        return driver.findElements(productCards).size();
    }

    public void clickFirstProduct() {
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        products.get(0).click();
    }
}
