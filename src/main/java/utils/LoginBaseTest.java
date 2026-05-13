package utils;

import org.testng.annotations.BeforeMethod;

public class LoginBaseTest extends BaseTest {
    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        driver.get(baseUrl + "smember");
    }
}
