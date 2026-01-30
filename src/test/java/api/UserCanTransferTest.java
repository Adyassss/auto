package api;
import api.generators.RandomData;
import api.models.comparison.ModelAssertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import api.requests.steps.AdminSteps;
import api.requests.steps.UserDepositSteps;
import api.requests.steps.UserProfileSteps;
import api.requests.steps.UserTransferSteps;

public class UserCanTransferTest {

    @Test
    public void userCanTransferMoney() {
        float amount = RandomData.getAmount();

        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        UserDepositSteps.depositMoney(userToken, senderId, amount);

        float balanceBefore = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        int receiverId = UserProfileSteps.createUserProfileId(userToken);

        UserTransferSteps.transferMoney(userToken, senderId, receiverId, amount);

        float balanceAfter = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        ModelAssertions.assertThatModels(balanceBefore-amount, balanceAfter).match();
    }

    //Negative cases

    @MethodSource("api.generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantTransferMoney(float amount) {
        float positiveAmount = RandomData.getAmount();

        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        UserDepositSteps.depositMoney(userToken, senderId, positiveAmount);

        float balanceBefore = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        int receiverId = UserProfileSteps.createUserProfileId(userToken);

        UserTransferSteps.transferMoneyWithInvalidData(userToken, senderId, receiverId, amount);

        float balanceAfter = UserProfileSteps.getUserProfileAccountBalance(userToken, senderId);

        ModelAssertions.assertThatModels(balanceBefore, balanceAfter).match();
    }
}
