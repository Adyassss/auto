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
import ui.pages.TransferPage;
import ui.pages.UserDashboard;


import static org.assertj.core.api.Assertions.assertThat;

public class UserCanTransferUI extends BaseUITest {
    

    @Test
    public void UserTransferWithCorrectDataTest() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();
        new LoginPage().open().login(Config.getProperty("admin.username"), Config.getProperty("admin.password"))
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
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();
        new LoginPage().open().login(Config.getProperty("admin.username"), Config.getProperty("admin.password"))
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
        .transferMoneyWithNotCorrectAmount()
        .checkAllertMassageAndAccept(BankAllerts.NOT_CORRECT_TRANSFER_AMOUNT.getMessage());
        

        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        
        float balanceProfile = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts[0].balance");
        assertThat(balanceProfile).isEqualTo(5000.0f);
    }
}
