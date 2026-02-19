package ui;

import java.util.Map;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.extensions.AdminSessionExtension;
import common.extensions.BrowserMatchExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;

import static org.assertj.core.api.Assertions.assertThat;

import api.BaseTest;
import api.configs.Config;

import static com.codeborne.selenide.Selenide.switchTo;

@ExtendWith(BrowserMatchExtension.class)
@ExtendWith(AdminSessionExtension.class)
public class BaseUITest extends BaseTest {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.baseUrl = Config.getProperty("baseUrl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("browserSize");
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true));
        Configuration.headless = true;
    }

    public <T> T checkAllertMassageAndAccept(String bankAllert) {
        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains(bankAllert);
        alert.accept();
        return (T) this;
    }

    public static String getAuthToken() {
        return Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');");
    }


}
