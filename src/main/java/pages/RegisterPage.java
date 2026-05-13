package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    private final By nameInput = By.xpath(
            "(//*[contains(normalize-space(),'Họ và tên')]/following::input[not(@type='hidden')][1])[1] | " +
                    "//input[@name='name' or contains(@placeholder,'Họ và tên')]"
    );
    private final By birthDateInput = By.xpath(
            "(//*[contains(normalize-space(),'Ngày sinh')]/following::input[not(@type='hidden')][1])[1] | " +
                    "//input[contains(@placeholder,'Ngày sinh') or contains(@name,'birth') or contains(@id,'birth')]"
    );
    private final By phoneInput = By.xpath(
            "(//*[contains(normalize-space(),'Số điện thoại')]/following::input[not(@type='hidden')][1])[1] | " +
                    "//input[@name='phone' or (@type='tel' and not(@name='search'))]"
    );
    private final By emailInput = By.xpath(
            "(//*[contains(normalize-space(),'Email')]/following::input[@type='email' or @type='text'][1])[1] | " +
                    "//input[@name='email' or @type='email']"
    );
    private final By passwordInputs = By.xpath("//input[@type='password']");
    private final By completeRegisterButton = By.xpath(
            "//button[contains(normalize-space(.),'Hoàn tất đăng ký')]"
    );

    private final By toastContainer = By.xpath(
            "//div[contains(@class,'Toastify__toast-body')] | " +
                    "//div[contains(@class,'Toastify__toast') and not(contains(@class,'Toastify__toast-container'))] | " +
                    "//div[contains(@class,'toast') and contains(@class,'show')] | " +
                    "//*[@role='alert']"
    );

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Chờ trang đăng ký tải xong (nút Hoàn tất + các ô chính)")
    public void waitForRegisterFormReady() {
        wait.until(ExpectedConditions.presenceOfElementLocated(completeRegisterButton));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
    }

    @Step("Kiểm tra form đăng ký Smember hiển thị đủ trường")
    public boolean isSmemberRegisterFormDisplayed() {
        try {
            findNameField();
            wait.until(ExpectedConditions.visibilityOfElementLocated(birthDateInput));
            findPhoneField();
            findEmailField();
            List<WebElement> pw = driver.findElements(passwordInputs);
            return pw.size() >= 2;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    @Step("Nhập họ tên")
    public void enterName(String name) {
        WebElement input = findNameField();
        robustClear(input);
        if (name != null && !name.isEmpty()) {
            input.sendKeys(name);
        }
    }

    private WebElement registerForm() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//form[.//button[contains(normalize-space(.),'Hoàn tất đăng ký')]]")
        ));
    }

    private WebElement findNameField() {
        try {
            return registerForm().findElement(By.xpath(
                    ".//input[@name='name' or @name='fullName' or @name='full_name' or contains(@placeholder,'Họ')]"
            ));
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@name='name' or @name='fullName' or @name='full_name']")));
            } catch (TimeoutException e2) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
            }
        }
    }

    public String getNameValue() {
        return Optional.ofNullable(findNameField().getAttribute("value")).orElse("");
    }

    private void robustClear(WebElement input) {
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.clear();
    }

    @Step("Kiểm tra ô họ tên đang báo lỗi (aria-invalid / class)")
    public boolean isNameFieldInvalid() {
        try {
            WebElement name = findNameField();
            if ("true".equalsIgnoreCase(name.getAttribute("aria-invalid"))) {
                return true;
            }
            String cls = name.getAttribute("class");
            if (cls != null && (cls.contains("error") || cls.contains("invalid") || cls.contains("danger"))) {
                return true;
            }
            WebElement parent = name.findElement(By.xpath("./ancestor::div[contains(@class,'form') or contains(@class,'field')][1]"));
            String ptxt = parent.getText();
            return ptxt.contains("Họ") && (ptxt.contains("trống") || ptxt.contains("bắt buộc"));
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Nhập ngày sinh (dd/MM/yyyy)")
    public void enterBirthDate(String birthDate) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(birthDateInput));
        robustClear(input);
        if (birthDate != null && !birthDate.isEmpty()) {
            input.sendKeys(birthDate);
        }
    }

    @Step("Nhập số điện thoại")
    public void enterPhone(String phone) {
        WebElement input = findPhoneField();
        robustClear(input);
        if (phone != null && !phone.isEmpty()) {
            input.sendKeys(phone);
        }
    }

    private WebElement findPhoneField() {
        try {
            return registerForm().findElement(By.xpath(".//input[@name='phone' or @type='tel']"));
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@name='phone' or @name='tel' or @name='phoneNumber']")));
            } catch (TimeoutException e2) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
            }
        }
    }

    @Step("Nhập email")
    public void enterEmail(String email) {
        WebElement input = findEmailField();
        robustClear(input);
        if (email != null && !email.isEmpty()) {
            input.sendKeys(email);
        }
    }

    private WebElement findEmailField() {
        try {
            return registerForm().findElement(By.xpath(".//input[@name='email' or @type='email']"));
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                        "//form//input[@name='email' or @type='email'] | //input[@name='email' and not(@type='hidden')]"
                )));
            } catch (TimeoutException e2) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
            }
        }
    }

    private List<WebElement> findPasswordFields() {
        try {
            List<WebElement> scoped = registerForm().findElements(By.xpath(".//input[@type='password']"));
            if (scoped.size() >= 2) {
                return scoped;
            }
        } catch (Exception ignored) {
        }
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(passwordInputs));
    }

    @Step("Nhập mật khẩu")
    public void enterPassword(String password) {
        List<WebElement> inputs = findPasswordFields();
        WebElement first = inputs.get(0);
        robustClear(first);
        if (password != null && !password.isEmpty()) {
            first.sendKeys(password);
        }
    }

    @Step("Nhập xác nhận mật khẩu")
    public void enterConfirmPassword(String password) {
        List<WebElement> inputs = findPasswordFields();
        if (inputs.size() < 2) {
            throw new IllegalStateException("Không tìm thấy ô xác nhận mật khẩu (cần 2 ô password).");
        }
        WebElement second = inputs.get(1);
        robustClear(second);
        if (password != null && !password.isEmpty()) {
            second.sendKeys(password);
        }
    }

    @Step("Nhấn Hoàn tất đăng ký")
    public void clickCompleteRegister() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(completeRegisterButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Step("Điền form đăng ký")
    public void fillRegisterForm(String name, String birthDate, String phone, String email,
                                 String password, String confirmPassword) {
        enterName(name);
        enterBirthDate(birthDate);
        enterPhone(phone);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
    }

    public String getPhoneFieldValue() {
        WebElement input = findPhoneField();
        String v = input.getAttribute("value");
        return v != null ? v : "";
    }

    public boolean pageContainsText(String fragment) {
        String body = driver.findElement(By.tagName("body")).getText();
        return body != null && body.contains(fragment);
    }

    @Step("Chờ và đọc nội dung toast gần nhất")
    public String waitForToastText() {
        try {
            WebElement el = shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastContainer));
            String t = el.getText().trim();
            if (!t.isEmpty()) {
                return t;
            }
        } catch (TimeoutException ignored) {
        }
        List<WebElement> any = driver.findElements(By.xpath("//div[contains(@class,'Toastify__toast')]"));
        for (int i = any.size() - 1; i >= 0; i--) {
            try {
                if (any.get(i).isDisplayed()) {
                    String t = any.get(i).getText().trim();
                    if (!t.isEmpty()) {
                        return t;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    public boolean waitForToastContaining(String fragment) {
        long deadline = System.currentTimeMillis() + 15000;
        while (System.currentTimeMillis() < deadline) {
            if (pageContainsText(fragment)) {
                return true;
            }
            String t = waitForToastText();
            if (!t.isEmpty() && t.contains(fragment)) {
                return true;
            }
            String ant = collectAntAndNoticeText();
            if (ant.contains(fragment)) {
                return true;
            }
            try {
                Thread.sleep(280);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return pageContainsText(fragment);
            }
        }
        return pageContainsText(fragment);
    }

    private String collectAntAndNoticeText() {
        StringBuilder sb = new StringBuilder();
        List<WebElement> nodes = driver.findElements(By.xpath(
                "//div[contains(@class,'ant-message')] | " +
                        "//div[contains(@class,'ant-notification')] | " +
                        "//div[contains(@class,'Toastify__toast-body')] | " +
                        "//div[contains(@class,'chakra-toast')] | " +
                        "//*[@data-testid='toast']"
        ));
        for (WebElement el : nodes) {
            try {
                if (el.isDisplayed()) {
                    sb.append(el.getText()).append(' ');
                }
            } catch (Exception ignored) {
            }
        }
        return sb.toString();
    }

    public boolean isOtpConfirmationScreen() {
        WebDriverWait otpWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            return Boolean.TRUE.equals(otpWait.until(d -> {
                String url = d.getCurrentUrl().toLowerCase();
                if (url.contains("otp") || url.contains("verify") || url.contains("xac-nhan")
                        || url.contains("confirmation") || url.contains("confirm")) {
                    return true;
                }
                try {
                    String body = d.findElement(By.tagName("body")).getText();
                    return body.contains("OTP") || body.contains("mã xác nhận") || body.contains("Xác nhận OTP")
                            || body.contains("Nhập mã") || body.contains("Mã OTP");
                } catch (Exception ex) {
                    return false;
                }
            }));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Trả về true nếu trong vài giây URL/nội dung giống bước OTP (dùng cho case lỗi: không được sang OTP). */
    public boolean appearsOtpWithinSeconds(int maxSeconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(maxSeconds));
        try {
            return Boolean.TRUE.equals(w.until(d -> {
                String url = d.getCurrentUrl().toLowerCase();
                if (url.contains("otp") || url.contains("verify") || url.contains("xac-nhan")
                        || url.contains("confirm")) {
                    return true;
                }
                try {
                    String body = d.findElement(By.tagName("body")).getText();
                    return (body.contains("OTP") || body.contains("mã xác nhận"))
                            && (body.contains("Nhập") || body.contains("Xác nhận"));
                } catch (Exception ex) {
                    return false;
                }
            }));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasInlineOrVisibleMessage(String messageFragment) {
        if (pageContainsText(messageFragment)) {
            return true;
        }
        List<WebElement> candidates = driver.findElements(By.xpath(
                "//*[contains(@class,'error') or contains(@class,'invalid') or contains(@class,'danger') or @role='alert']"
        ));
        return candidates.stream().anyMatch(e -> {
            try {
                return e.getText().contains(messageFragment);
            } catch (Exception ex) {
                return false;
            }
        });
    }

    public boolean hasRedHintForPasswordRule() {
        List<WebElement> hints = driver.findElements(By.xpath(
                "//*[contains(@class,'error') or contains(@class,'invalid') or contains(@class,'hint') or contains(@class,'help')]"
        ));
        String needle = "Mật khẩu tối thiểu";
        return hints.stream().anyMatch(e -> {
            try {
                String t = e.getText();
                return t != null && t.contains(needle);
            } catch (Exception ex) {
                return false;
            }
        }) || pageContainsText(needle);
    }

    public List<String> visibleErrorTexts() {
        return driver.findElements(By.xpath(
                "//*[contains(@class,'error') or contains(@class,'invalid') or contains(@class,'text-red')]"
        )).stream().map(WebElement::getText).filter(s -> s != null && !s.isBlank()).collect(Collectors.toList());
    }
}
