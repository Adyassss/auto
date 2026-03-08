package api;
import api.generators.RandomData;
import api.models.AccountModel;
import api.models.comparison.ModelAssertions;

import api.requests.steps.DataBaseSteps;
import dao.AccountDao;
import dao.comparison.DaoAndModelAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import api.requests.steps.AdminSteps;
import api.requests.steps.UserDepositSteps;
import api.requests.steps.UserProfileSteps;


public class UserCanDepositTest {

    @Test
    public void userCanDeposit() {
        float amount = RandomData.getAmount();
        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        AccountModel balanceBeforeProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao before = DataBaseSteps.getAccountById(balanceBeforeProfile.getId());

        DaoAndModelAssertions.assertThat(balanceBeforeProfile, before).match();

        UserDepositSteps.depositMoney(userToken, senderId, amount);

        AccountModel balanceAfterProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao after = DataBaseSteps.getAccountById(balanceAfterProfile.getId());

        DaoAndModelAssertions.assertThat(balanceAfterProfile, after).match();


    }

    @MethodSource("api.generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantDepositWithInvalidData(float amount) {

        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);

        AccountModel balanceBeforeProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao before = DataBaseSteps.getAccountById(balanceBeforeProfile.getId());

        DaoAndModelAssertions.assertThat(balanceBeforeProfile, before).match();

        UserDepositSteps.depositMoneyWithInvalidData(userToken, senderId, amount);

        AccountModel balanceAfterProfile = UserProfileSteps.getProfileAccountBalance(userToken, senderId);

        AccountDao after = DataBaseSteps.getAccountById(balanceAfterProfile.getId());

        DaoAndModelAssertions.assertThat(balanceAfterProfile, after).match();

    }
}


