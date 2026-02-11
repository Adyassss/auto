package ui;

import api.models.UserProfileModelResponse;

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
    public void userTransferWithCorrectDataTest() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();
        new LoginPage().open().login(Config.getProperty(Config.ADMIN_USERNAME_KEY), Config.getProperty(Config.ADMIN_PASSWORD_KEY))
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
        
        
       
        String token = getAuthToken();
       
        UserProfileModelResponse profileSecondAccount = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileModelResponse.class);
        float balanceSecondAccount = profileSecondAccount.getAccounts().get(1).getBalance();

        UserProfileModelResponse profileFirstAccount = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileModelResponse.class);

        float balanceFirstAccount = profileFirstAccount.getAccounts().get(0).getBalance();
        assertThat(balanceSecondAccount).isCloseTo(5000.0f, org.assertj.core.data.Offset.offset(0.01f));
        assertThat(balanceFirstAccount).isCloseTo(0.0f, org.assertj.core.data.Offset.offset(0.01f));
    }

    @Test
    public void userTransferWithNotCorrectDataTest() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();
        new LoginPage().open().login(Config.getProperty(Config.ADMIN_USERNAME_KEY), Config.getProperty(Config.ADMIN_PASSWORD_KEY))
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


        String token = getAuthToken();

        UserProfileModelResponse profileSecondAccount = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileModelResponse.class);
        float balanceSecondAccount = profileSecondAccount.getAccounts().get(0).getBalance();

        UserProfileModelResponse profileFirstAccount = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileModelResponse.class);

        float balanceFirstAccount = profileSecondAccount.getAccounts().get(1).getBalance();
        assertThat(balanceSecondAccount).isCloseTo(5000.0f, org.assertj.core.data.Offset.offset(0.01f));
        assertThat(balanceFirstAccount).isCloseTo(0.0f, org.assertj.core.data.Offset.offset(0.01f));
    }
}
