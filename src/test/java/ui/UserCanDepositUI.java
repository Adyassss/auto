package ui;

import api.models.UserProfileModelResponse;

import api.generators.RandomData;
import common.annotations.AdminSession;
import common.annotations.Browsers;
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
    @AdminSession
    @Browsers({"chrome"})
    public void userDepositWithCorrectDataTest() {
        String name = RandomData.getUsername();
        String password = RandomData.getPassword();
        new AdminPanel().open()
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
        
        
        String token = getAuthToken();

        UserProfileModelResponse profileUserAfterDeposit = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileModelResponse.class);
        float balanceProfile = profileUserAfterDeposit.getAccounts().get(0).getBalance();
        assertThat(balanceProfile).isEqualTo(5000.0f);
    }
    @Test
    @AdminSession
    public void userDepositWithNotCorrectDataTest() {
        String name = RandomData.getUsername();
        String password = RandomData.getPassword();
        new AdminPanel().open()
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
        
        
        String token = getAuthToken();
        UserProfileModelResponse profileUserAfterDeposit = new CrudRequesters(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileModelResponse.class);
        float balanceProfile = profileUserAfterDeposit.getAccounts().get(0).getBalance();
        assertThat(balanceProfile).isEqualTo(0.0f);
    }
}
