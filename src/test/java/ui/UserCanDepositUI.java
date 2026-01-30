package ui;

import com.codeborne.selenide.Selenide;

import api.configs.Config;
import api.generators.RandomData;
import org.junit.jupiter.api.Test;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;
import ui.pages.AdminPanel;
import ui.pages.DepositPage;
import ui.pages.LoginPage;
import ui.pages.UserDashboard;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCanDepositUI extends BaseUITest {

    @Test
    public void UserDepositWithCorrectDataTest() {
        String name = RandomData.getUsername();
        String password = RandomData.getPassword();
        new LoginPage().open().login(Config.getProperty("admin.username"), Config.getProperty("admin.password"))
        .getPage(AdminPanel.class)
        .ensureAdminPanelVisible()
        .createUser(name, password).checkAllertMassageAndAccept(BankAllerts.USER_CREATED_SUCCESSFULLY.getMessage()).logout()
        .getPage(LoginPage.class)
        .open().login(name, password)
        .getPage(UserDashboard.class)
        .ensureDashboardVisible()
        .createAccount()
        .checkAllertMassageAndAccept(BankAllerts.ACCOUNT_CREATED_SUCCESSFULLY.getMessage())
        .getPage(DepositPage.class)
        .open()
        .ensureDepositPageVisible()
        .depositMoney()
        .checkAllertMassageAndAccept(BankAllerts.DEPOSIT_MONEY_SUCCESSFULLY.getMessage());
        
        
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        float balanceProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[0].balance");
        assertThat(balanceProfile).isEqualTo(5000.0f);
    }
    @Test
    public void UserDepositWithNotCorrectDataTest() {
        String name = RandomData.getUsername();
        String password = RandomData.getPassword();
        new LoginPage().open().login(Config.getProperty("admin.username"), Config.getProperty("admin.password"))
        .getPage(AdminPanel.class)
        .ensureAdminPanelVisible()
        .createUser(name, password).checkAllertMassageAndAccept(BankAllerts.USER_CREATED_SUCCESSFULLY.getMessage()).logout()
        .getPage(LoginPage.class)
        .open().login(name, password)
        .getPage(UserDashboard.class)
        .ensureDashboardVisible()
        .createAccount()
        .checkAllertMassageAndAccept(BankAllerts.ACCOUNT_CREATED_SUCCESSFULLY.getMessage())
        .getPage(DepositPage.class)
        .open()
        .ensureDepositPageVisible()
        .depositMoneyWithInvalidAmount()
        .checkAllertMassageAndAccept(BankAllerts.NOT_CORRECT_DEPOSIT_AMOUNT.getMessage());
        
        
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        float balanceProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[0].balance");
        assertThat(balanceProfile).isEqualTo(0.0f);
    }
}
