package api;

import api.generators.RandomData;
import api.models.UserProfileResponseModel;
import api.models.comparison.ModelAssertions;
import api.requests.steps.AdminSteps;
import api.requests.steps.ChangeNameSteps;
import api.requests.steps.DataBaseSteps;
import api.requests.steps.UserProfileSteps;
import dao.UserDao;
import dao.comparison.DaoAndModelAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class UserCanChangeUsernameTest {

    @Test
    public void userCanChangeUsername() {
        String name = RandomData.getName();

        String userToken = AdminSteps.createToken();

        ChangeNameSteps.changeName(userToken, name);

        UserProfileResponseModel profileAfter = UserProfileSteps.getUserProfile(userToken);

        UserDao profileInDB = DataBaseSteps.getUserById(profileAfter.getId());

        DaoAndModelAssertions.assertThat(profileAfter, profileInDB).match();

    }

    @ParameterizedTest
    @MethodSource("api.generators.RandomData#NegativeNames")
    public void userCantChangeUsernameWithInvalidData(String invalidName) {
        String userToken = AdminSteps.createToken();

        ChangeNameSteps.changeNameWithInvalidData(userToken, invalidName);

        UserProfileResponseModel profileAfter = UserProfileSteps.getUserProfile(userToken);

        UserDao profileInDB = DataBaseSteps.getUserById(profileAfter.getId());

        DaoAndModelAssertions.assertThat(profileAfter, profileInDB).match();
    }
}
