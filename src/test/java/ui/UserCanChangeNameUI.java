package ui;

import com.codeborne.selenide.Selenide;

import api.generators.RandomData;
import api.models.AdminCanCreateUserRequest;
import api.models.UserProfileResponseModel;

import org.junit.jupiter.api.Test;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.ValidatedCrudRequester;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;
import ui.pages.AdminPanel;
import ui.pages.LoginPage;
import ui.pages.UserDashboard;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCanChangeNameUI extends BaseUITest {


    @Test
    public void UserChangeNameWithCorrectDataTest() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.getName();

        // Логинимся под админом и создаем юзера
        AdminCanCreateUserRequest admin = AdminCanCreateUserRequest.getAdmin();
        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
                .getPage(AdminPanel.class)
                .ensureAdminPanelVisible()
                .createUser(username, password)
                .checkAllertMassageAndAccept(BankAllerts.USER_CREATED_SUCCESSFULLY.getMessage())
                .logout();

        // Логинимся под юзером и меняем имя
        new LoginPage().open().login(username, password)
                .getPage(UserDashboard.class)
                .ensureDashboardVisible()
                .changeName(name)
                .checkAllertMassageAndAccept(BankAllerts.NAME_UPDATED_SUCCESSFULLY.getMessage());
        

        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        String nameProfile = new ValidatedCrudRequester<UserProfileResponseModel>(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .getName();
        assertThat(nameProfile.equals(name)).isTrue();
    }


    @Test
    public void UserChangeNameWithNotCorrectDataTest() {
        String username = RandomData.getUsername();
        String password = RandomData.getPassword();
        String name = RandomData.negativeName();

        // Логинимся под админом и создаем юзера
        AdminCanCreateUserRequest admin = AdminCanCreateUserRequest.getAdmin();
        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
                .getPage(AdminPanel.class)
                .ensureAdminPanelVisible()
                .createUser(username, password)
                .checkAllertMassageAndAccept(BankAllerts.USER_CREATED_SUCCESSFULLY.getMessage())
                .logout()
                .getPage(LoginPage.class)
                .open()
                .login(username, password)
                .getPage(UserDashboard.class)
                .ensureDashboardVisible()
                .changeName(name)
                .checkAllertMassageAndAccept(BankAllerts.ENTER_VALID_NAME.getMessage());


        
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        UserProfileResponseModel nameProfile = new ValidatedCrudRequester<UserProfileResponseModel>
        (RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get();
        assertThat(nameProfile.getName()).isNull();
    }
}

