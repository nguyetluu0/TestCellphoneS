package utils;

import org.testng.annotations.BeforeMethod;

public class ProductListBaseTest extends BaseTest {
    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        driver.get(baseUrl + "mobile.html");
    }
}
