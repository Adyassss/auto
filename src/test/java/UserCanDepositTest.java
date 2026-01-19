import generators.RandomData;
import models.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.AdminCreateUser;
import requests.DepositRequest;
import requests.UserCreateAccRequest;
import requests.UserProfileRequest;
import specs.RequestSpec;
import specs.ResponseSpec;


public class UserCanDepositTest extends BaseTest {

    //Positive cases
    @MethodSource("generators.RandomData#PositiveAmount")
    @ParameterizedTest
    public void userCanDeposit(float amount) {

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

        float balanceBefore = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        new DepositRequest(RequestSpec.userRequest(userToken),ResponseSpec.ok())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(amount)
                        .build());

        float balanceAfter = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        softly.assertThat(balanceAfter)
                .isEqualTo(amount+balanceBefore);

        softly.assertAll();
    }

//     Negative test cases
    @MethodSource("generators.RandomData#NegativeAmount")
    @ParameterizedTest
    public void userCantDepositWithInvalidData(float amount) {

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

        float balanceBefore = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        new DepositRequest(RequestSpec.userRequest(userToken),ResponseSpec.badRequest())
                .post(UserDepositModelRequest.builder()
                        .id(senderId)
                        .balance(amount)
                        .build());

        float balanceAfter = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("accounts[0].balance");

        softly.assertThat(balanceAfter)
                .isEqualTo(balanceBefore);

        softly.assertAll();
    }
}

