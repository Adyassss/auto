import generators.RandomData;
import models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import requests.steps.AdminSteps;
import specs.RequestSpec;
import specs.ResponseSpec;


public class UserCanDepositTest extends BaseTest {
//    Positive cases
//    @MethodSource("generators.RandomData#PositiveAmount")
//    @ParameterizedTest
    @Test
    public void userCanDeposit() {
        float amount = RandomData.getAmount();
        String userToken = AdminSteps.createUser();

        int senderId = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");

        float balanceBefore = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.DEPOSIT_USER,
                ResponseSpec.ok())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(amount)
                        .build());

        float balanceAfter = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        softly.assertThat(balanceAfter)
                .isEqualTo(amount+balanceBefore);

        softly.assertAll();
    }

 //    Negative test cases
    @MethodSource("generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantDepositWithInvalidData(float amount) {

        String userToken = AdminSteps.createUser();

        int senderId = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");

        float balanceBefore = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.DEPOSIT_USER,
                ResponseSpec.badRequest())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(amount)
                        .build());

        float balanceAfter = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        softly.assertThat(balanceAfter)
                .isEqualTo(balanceBefore);

        softly.assertAll();

    }
}


