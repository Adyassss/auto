package ui;

import java.util.Map;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Alert;
import static org.assertj.core.api.Assertions.assertThat;

import api.BaseTest;
import api.configs.Config;
import static com.codeborne.selenide.Selenide.switchTo;
public class BaseUITest extends BaseTest {
    public static String secondAcc = "";

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.baseUrl = Config.getProperty("baseUrl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("browserSize");
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true));
    }

    public <T> T checkAllertMassageAndAccept(String bankAllert){
        int count = 0;
        if (bankAllert.contains("New Account Created! Account Number:"))
        {
            count++;
        }
        if (count >= 1) {
            String[] filter = bankAllert.split(" ");
            secondAcc = filter[filter.length - 1];
        }
        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains(bankAllert);
        alert.accept();
        return (T) this;
    }


}
