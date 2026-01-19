import generators.RandomData;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.*;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import requests.AdminCreateUser;
import requests.DepositRequest;
import requests.UserCreateAccRequest;
import requests.UserProfileRequest;
import specs.RequestSpec;
import specs.ResponseSpec;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

