import generators.RandomData;
import models.comparison.ModelAssertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.steps.AdminSteps;
import requests.steps.UserDepositSteps;
import requests.steps.UserProfileSteps;

public class UserCanDepositTest {

    @Test
    public void userCanDeposit() {
        float amount = RandomData.getAmount();
        String userToken = AdminSteps.createUser();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        float balanceBefore = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        UserDepositSteps.depositMoney(userToken, senderId, amount);

        float balanceAfter = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        ModelAssertions.assertThatModels(balanceBefore+amount, balanceAfter).match();
    }

    @MethodSource("generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantDepositWithInvalidData(float amount) {

        String userToken = AdminSteps.createUser();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        float balanceBefore = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        UserDepositSteps.depositMoneyWithInvalidData(userToken, senderId, amount);

        float balanceAfter = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        ModelAssertions.assertThatModels(balanceBefore, balanceAfter).match();

    }
}


