package ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import generators.RandomData;
import models.AdminCanCreateUserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import specs.RequestSpec;
import specs.ResponseSpec;

import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

public class UserCanChangeNameUI {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.baseUrl = "http://localhost:3000";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true));
    }

    @Test
    public void UserChangeNameWithCorrectDataTest() {
        AdminCanCreateUserRequest admin = AdminCanCreateUserRequest.builder()
                .username("admin")
                .password("admin")
                .build();
        Selenide.open("/login");
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(admin.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(admin.getPassword());
        $("button").click();
        $(Selectors.byText("Admin Panel")).shouldBe(Condition.visible);
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(username);
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(password);
        $(".btn.btn-primary.w-100").click();
        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains("User created successfully!");
        alert.accept();
        $(".btn.btn-danger").click();
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(username);
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(password);
        $(".btn.btn-primary.w-100")
                .shouldBe(Condition.visible)
                .click();
        $(Selectors.byText("User Dashboard")).shouldBe(Condition.visible);
        $(Selectors.byText("Noname")).shouldBe(Condition.visible).click();
        $(Selectors.byAttribute("placeholder", "Enter new name")).sendKeys(name);
        $(Selectors.byText("\uD83D\uDCBE Save Changes")).shouldBe(Condition.visible).click();
        Alert alert1 = switchTo().alert();
        assertThat(alert1.getText()).contains("✅ Name updated successfully!");
        alert1.accept();
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        String nameProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("name");
        assertThat(nameProfile.equals(name)).isTrue();
    }


    @Test
    public void UserChangeNameWithNotCorrectDataTest() {
        AdminCanCreateUserRequest admin = AdminCanCreateUserRequest.builder()
                .username("admin")
                .password("admin")
                .build();
        Selenide.open("/login");
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(admin.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(admin.getPassword());
        $("button").click();
        $(Selectors.byText("Admin Panel")).shouldBe(Condition.visible);
        String name = RandomData.getUsername();
        String password = RandomData.getPassword();
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(name);
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(password);
        $(".btn.btn-primary.w-100").click();
        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains("✅ User created successfully!");
        alert.accept();
        $(".btn.btn-danger").click();
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(name);
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(password);
        $(".btn.btn-primary.w-100")
                .shouldBe(Condition.visible)
                .click();
        $(Selectors.byText("User Dashboard")).shouldBe(Condition.visible);
        $(Selectors.byText("Noname")).shouldBe(Condition.visible).click();
        $(Selectors.byAttribute("placeholder", "Enter new name")).sendKeys(RandomData.negativeName());
        $(Selectors.byText("\uD83D\uDCBE Save Changes")).shouldBe(Condition.visible).click();
        Alert alert1 = switchTo().alert();
        assertThat(alert1.getText().contains("Name must contain two words with letters only"));
        alert1.accept();
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );

        String nameProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("name");
        assertThat(nameProfile).isNull();
    }
}

