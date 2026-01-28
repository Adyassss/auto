import generators.RandomData;
import models.comparison.ModelAssertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.steps.AdminSteps;
import requests.steps.ChangeNameSteps;
import requests.steps.UserProfileSteps;


public class UserCanChangeUsernameTest {

    @Test
    public void userCanChangeUsername() {
        String name = RandomData.getName();

        String userToken = AdminSteps.createUser();
        
        ChangeNameSteps.changeName(userToken, name);

        String nameAfter = UserProfileSteps.getUserProfileName(userToken);

        ModelAssertions.assertThatModels(name, nameAfter).match();

    }

    @ParameterizedTest
    @MethodSource("generators.RandomData#NegativeNames")
    public void userCantChangeUsernameWithInvalidData(String invalidName) {
        String userToken = AdminSteps.createUser();

        String nameBefore = UserProfileSteps.getUserProfileName(userToken);

        ChangeNameSteps.changeNameWithInvalidData(userToken, invalidName);

        String nameAfter = UserProfileSteps.getUserProfileName(userToken);

        ModelAssertions.assertThatModels(nameBefore, nameAfter).match();
    }
}
