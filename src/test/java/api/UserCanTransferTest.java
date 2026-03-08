package api;
import api.generators.RandomData;
import api.models.AccountModel;

import api.requests.steps.*;
import dao.AccountDao;
import dao.comparison.DaoAndModelAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class UserCanTransferTest {

    @Test
    public void userCanTransferMoney() {
        float amount = RandomData.getAmount();

        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        AccountModel balanceBeforeProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao before = DataBaseSteps.getAccountById(balanceBeforeProfile.getId());

        DaoAndModelAssertions.assertThat(balanceBeforeProfile, before).match();

        UserDepositSteps.depositMoney(userToken, senderId, amount);

        int receiverId = UserProfileSteps.createUserProfileId(userToken);

        UserTransferSteps.transferMoney(userToken, senderId, receiverId, amount);

        AccountModel balanceAfterProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao after = DataBaseSteps.getAccountById(balanceAfterProfile.getId());

        DaoAndModelAssertions.assertThat(balanceAfterProfile, after).match();
    }

    //Negative cases

    @MethodSource("api.generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantTransferMoney(float amount) {
        float positiveAmount = RandomData.getAmount();

        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        AccountModel balanceBeforeProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao before = DataBaseSteps.getAccountById(balanceBeforeProfile.getId());

        DaoAndModelAssertions.assertThat(balanceBeforeProfile, before).match();

        UserDepositSteps.depositMoney(userToken, senderId, positiveAmount);

        int receiverId = UserProfileSteps.createUserProfileId(userToken);

        UserTransferSteps.transferMoneyWithInvalidData(userToken, senderId, receiverId, amount);

        AccountModel balanceAfterProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao after = DataBaseSteps.getAccountById(balanceAfterProfile.getId());

        DaoAndModelAssertions.assertThat(balanceAfterProfile, after).match();
    }
}
