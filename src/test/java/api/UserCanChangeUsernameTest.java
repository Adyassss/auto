package api;

import api.models.AdminCanCreateUserRequest;
import api.requests.steps.BaseSteps;
import common.storage.SessionStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import api.generators.RandomData;
import api.models.comparison.ModelAssertions;
import api.requests.steps.AdminSteps;
import api.requests.steps.ChangeNameSteps;
import api.requests.steps.UserProfileSteps;


public class UserCanChangeUsernameTest {

    @Test
    public void userCanChangeUsername() {
        String name = RandomData.getName();

        String userToken = AdminSteps.createToken();
        
        ChangeNameSteps.changeName(userToken, name);

        String nameAfter = UserProfileSteps.getUserProfileName(userToken);

        ModelAssertions.assertThatModels(name, nameAfter).match();

    }

    @ParameterizedTest
    @MethodSource("api.generators.RandomData#NegativeNames")
    public void userCantChangeUsernameWithInvalidData(String invalidName) {
        String userToken = AdminSteps.createToken();

        String nameBefore = UserProfileSteps.getUserProfileName(userToken);

        ChangeNameSteps.changeNameWithInvalidData(userToken, invalidName);

        String nameAfter = UserProfileSteps.getUserProfileName(userToken);

        ModelAssertions.assertThatModels(nameBefore, nameAfter).match();
    }
}
