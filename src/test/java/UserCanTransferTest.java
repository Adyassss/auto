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

public class UserCanTransferTest extends BaseTest {

//    // Positive cases
    @Test
    public void userCanTransferMoney() {
        float amount = RandomData.getAmount();

        String userToken = AdminSteps.createUser();

        int senderId = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                        .extract()
                        .path("id");

        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.DEPOSIT_USER,
                ResponseSpec.ok())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(amount)
                        .build());

        float balanceBefore = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        int receiverId = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");


        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.TRANSFER_USER,
                ResponseSpec.ok())
                .post(UserCanTransferRequestModel.builder()
                        .senderAccountId(senderId)
                        .receiverAccountId(receiverId)
                        .amount(amount)
                        .build());

        float balanceAfter = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        softly.assertThat(balanceBefore-amount)
                .isEqualTo(balanceAfter);

        softly.assertAll();
    }

    //Negative cases

    @MethodSource("generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantTransferMoney(float amount) {
        float positiveAmount = RandomData.getAmount();

        String userToken = AdminSteps.createUser();

        int senderId = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");

        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.DEPOSIT_USER,
                ResponseSpec.ok())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(positiveAmount)
                        .build());

        float balanceBefore = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        int receiverId = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");


        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.TRANSFER_USER,
                ResponseSpec.badRequest())
                .post(UserCanTransferRequestModel.builder()
                        .senderAccountId(senderId)
                        .receiverAccountId(receiverId)
                        .amount(amount)
                        .build());

        float balanceAfter = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("accounts.find { it.id == " + senderId + " }.balance");

        softly.assertThat(balanceBefore)
                .isEqualTo(balanceAfter);

        softly.assertAll();
    }
}
