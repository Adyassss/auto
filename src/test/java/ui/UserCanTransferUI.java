package ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.generators.RandomData;
import api.models.AdminCanCreateUserRequest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;
import ui.pages.AdminPanel;
import ui.pages.DepositPage;
import ui.pages.LoginPage;
import ui.pages.TransferPage;
import ui.pages.UserDashboard;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

public class UserCanTransferUI extends BaseUITest {
    

    @Test
    public void UserTransferWithCorrectDataTest() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();
        String secondAcc = "";
        AdminCanCreateUserRequest admin = AdminCanCreateUserRequest.getAdmin();
        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
        .getPage(AdminPanel.class)
        .ensureAdminPanelVisible()
        .createUser(username, password)
        .checkAllertMassageAndAccept(BankAllerts.USER_CREATED_SUCCESSFULLY.getMessage()).logout()
        .getPage(LoginPage.class)
        .open()
        .login(username, password)
        .getPage(UserDashboard.class)
        .ensureDashboardVisible()
        .createAccount()
        .checkAllertMassageAndAccept(BankAllerts.ACCOUNT_CREATED_SUCCESSFULLY.getMessage())
        .createAccount()
        .checkAllertMassageAndAccept(BankAllerts.ACCOUNT_CREATED_SUCCESSFULLY.getMessage())
        .getPage(UserDashboard.class)
        .open()
        .changeName(name)
        .getPage(DepositPage.class)
        .open()
        .depositMoney()
        .checkAllertMassageAndAccept(BankAllerts.DEPOSIT_MONEY_SUCCESSFULLY.getMessage())
        .getPage(TransferPage.class)
        .open()
        .transferMoney()
        .checkAllertMassageAndAccept(BankAllerts.TRANSFER_MONEY_SUCCESSFULLY.getMessage());
        
        
       
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
       
        float balanceSecondAccount = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[1].balance");
        
        float balanceFirstAccount = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[0].balance");
        assertThat(balanceSecondAccount).isCloseTo(5000.0f, org.assertj.core.data.Offset.offset(0.01f));
        assertThat(balanceFirstAccount).isCloseTo(0.0f, org.assertj.core.data.Offset.offset(0.01f));
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
        assertThat(balanceProfile).isCloseTo(0.0f, org.assertj.core.data.Offset.offset(0.01f));
    }
}
