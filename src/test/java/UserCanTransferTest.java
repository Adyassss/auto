import generators.RandomData;
import models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import requests.*;
import specs.RequestSpec;
import specs.ResponseSpec;

public class UserCanTransferTest extends BaseTest {

//    // Positive cases
    @Test
    public void userCanTransferMoney() {
        float amount = RandomData.getAmount();

        String userToken = new AdminCreateUser(RequestSpec.adminRequest(), ResponseSpec.created())
                .post(AdminCanCreateUserRequest.builder()
                        .username(RandomData.getUsername())
                        .password(RandomData.getPassword())
                        .role(UserRole.USER.toString())
                        .build())
                .extract()
                .header("Authorization");

        int senderId = new UserCreateAccRequest(RequestSpec.userRequest(userToken),ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                        .extract()
                        .path("id");

        new DepositRequest(RequestSpec.userRequest(userToken),ResponseSpec.ok())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(amount)
                        .build());

        float balanceBefore = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        int receiverId = new UserCreateAccRequest(RequestSpec.userRequest(userToken),ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");


        new UserCanTransferRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(UserCanTransferRequestModel.builder()
                        .senderAccountId(senderId)
                        .receiverAccountId(receiverId)
                        .amount(amount)
                        .build());

        float balanceAfter = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        softly.assertThat(balanceBefore-amount)
                .isEqualTo(balanceAfter);

        softly.assertAll();
//        float expectedAfter = beforeBalance + amount;
//        assertEquals(expectedAfter, afterBalance, 0.001);
//
    }

    //Negative cases

    @MethodSource("generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantTransferMoney(float amount) {
        float positiveAmount = RandomData.getAmount();

        String userToken = new AdminCreateUser(RequestSpec.adminRequest(), ResponseSpec.created())
                .post(AdminCanCreateUserRequest.builder()
                        .username(RandomData.getUsername())
                        .password(RandomData.getPassword())
                        .role(UserRole.USER.toString())
                        .build())
                .extract()
                .header("Authorization");

        int senderId = new UserCreateAccRequest(RequestSpec.userRequest(userToken),ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");

        new DepositRequest(RequestSpec.userRequest(userToken),ResponseSpec.ok())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(positiveAmount)
                        .build());

        float balanceBefore = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        int receiverId = new UserCreateAccRequest(RequestSpec.userRequest(userToken),ResponseSpec.created())
                .post(CreateUserRequestModel
                        .builder().build())
                .extract()
                .path("id");


        new UserCanTransferRequest(RequestSpec.userRequest(userToken), ResponseSpec.badRequest())
                .post(UserCanTransferRequestModel.builder()
                        .senderAccountId(senderId)
                        .receiverAccountId(receiverId)
                        .amount(amount)
                        .build());

        float balanceAfter = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        softly.assertThat(balanceBefore)
                .isEqualTo(balanceAfter);

        softly.assertAll();
    }
}
