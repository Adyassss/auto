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

public class UserCanTransferUI {
    @BeforeAll
    public static void setupSelenoid() {
        Configuration.baseUrl = "http://localhost:3000";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true));
    }

    @Test
    public void UserTransferWithCorrectDataTest() {
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
        assertThat(alert.getText()).contains("User created successfully!");
        alert.accept();
        $(".btn.btn-danger").click();
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(name);
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(password);
        $(".btn.btn-primary.w-100")
                .shouldBe(Condition.visible)
                .click();
        $(Selectors.byText("User Dashboard")).shouldBe(Condition.visible);
        $(Selectors.byText("➕ Create New Account")).shouldBe(Condition.visible).click();
        Alert alert1 = switchTo().alert();
        assertThat(alert1.getText().contains("✅ New Account Created! Account Number:"));
        alert1.accept();
        $(Selectors.byText("➕ Create New Account")).shouldBe(Condition.visible).click();
        Alert alert2 = switchTo().alert();
        assertThat(alert2.getText().contains("✅ New Account Created! Account Number:"));
        String border = alert2.getText();
        String[] filter = border.split(" ");
        String secondAcc = filter[filter.length - 1];
        alert2.accept();
        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).click();
        $("select").selectOption(1);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys("5000");
        $(Selectors.byText("\uD83D\uDCB5 Deposit")).click();
        Alert alert3 = switchTo().alert();
        assertThat(alert3.getText()).contains("Successfully deposited $5000 to account");
        alert3.accept();
        String newName = RandomData.getName();
        $(Selectors.byText("Noname")).shouldBe(Condition.visible).click();
        $(Selectors.byAttribute("placeholder", "Enter new name")).sendKeys(newName);
        $(Selectors.byText("\uD83D\uDCBE Save Changes")).shouldBe(Condition.visible).click();
        Alert alert4 = switchTo().alert();
        assertThat(alert4.getText().contains("✅ Name updated successfully!"));
        alert4.accept();
        $(Selectors.byText("\uD83C\uDFE0 Home")).shouldBe(Condition.visible).click();
        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible).click();
        $("select.account-selector")
                .shouldBe(Condition.visible)
                .selectOption(1);
        $(Selectors.byAttribute("placeholder", "Enter recipient name")).sendKeys(newName);
        $(Selectors.byAttribute("placeholder", "Enter recipient account number")).sendKeys(secondAcc);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys("5000");
        $(Selectors.byId("confirmCheck")).shouldBe(Condition.visible).click();
        $(Selectors.byText("\uD83D\uDE80 Send Transfer")).shouldBe(Condition.visible).click();
        Alert alert5 = switchTo().alert();
        assertThat(alert5.getText().contains("✅ Successfully transferred"));
        alert5.accept();
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        float balanceProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[1].balance");
        assertThat(balanceProfile).isEqualTo(5000);
    }

    @Test
    public void UserTransferWithNotCorrectDataTest() {
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
        assertThat(alert.getText()).contains("User created successfully!");
        alert.accept();
        $(".btn.btn-danger").click();
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(name);
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(password);
        $(".btn.btn-primary.w-100")
                .shouldBe(Condition.visible)
                .click();
        $(Selectors.byText("User Dashboard")).shouldBe(Condition.visible);
        $(Selectors.byText("➕ Create New Account")).shouldBe(Condition.visible).click();
        Alert alert1 = switchTo().alert();
        assertThat(alert1.getText().contains("✅ New Account Created! Account Number:"));
        alert1.accept();
        $(Selectors.byText("➕ Create New Account")).shouldBe(Condition.visible).click();
        Alert alert2 = switchTo().alert();
        assertThat(alert2.getText().contains("✅ New Account Created! Account Number:"));
        String border = alert2.getText();
        String[] filter = border.split(" ");
        String secondAcc = filter[filter.length - 1];
        alert2.accept();
        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).click();
        $("select").selectOption(1);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys("5000");
        $(Selectors.byText("\uD83D\uDCB5 Deposit")).click();
        Alert alert3 = switchTo().alert();
        assertThat(alert3.getText()).contains("Successfully deposited $5000 to account");
        alert3.accept();
        String newName = RandomData.getName();
        $(Selectors.byText("Noname")).shouldBe(Condition.visible).click();
        $(Selectors.byAttribute("placeholder", "Enter new name")).sendKeys(newName);
        $(Selectors.byText("\uD83D\uDCBE Save Changes")).shouldBe(Condition.visible).click();
        Alert alert4 = switchTo().alert();
        assertThat(alert4.getText().contains("✅ Name updated successfully!"));
        alert4.accept();
        $(Selectors.byText("\uD83C\uDFE0 Home")).shouldBe(Condition.visible).click();
        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible).click();
        $("select.account-selector")
                .shouldBe(Condition.visible)
                .selectOption(1);
        $(Selectors.byAttribute("placeholder", "Enter recipient name")).sendKeys(newName);
        $(Selectors.byAttribute("placeholder", "Enter recipient account number")).sendKeys(secondAcc);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys("10001");
        $(Selectors.byId("confirmCheck")).shouldBe(Condition.visible).click();
        $(Selectors.byText("\uD83D\uDE80 Send Transfer")).shouldBe(Condition.visible).click();
        Alert alert5 = switchTo().alert();
        assertThat(alert5.getText().contains("❌ Error: Transfer amount cannot exceed 10000"));
        alert5.accept();
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        float balanceProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[1].balance");
        assertThat(balanceProfile).isEqualTo(0);
    }
}
