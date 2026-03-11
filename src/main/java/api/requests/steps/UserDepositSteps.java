package api.requests.steps;

import api.models.UserDepositModelRequest;
import api.requests.skelethon.Endpoint;
import api.specs.RequestSpec;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserDepositSteps {
    public static ValidatableResponse depositMoney(String userToken, int senderId, float amount){
        Response response = given()
                .spec(RequestSpec.userRequest(userToken))
                .body(UserDepositModelRequest.builder()
                        .accountId(senderId)
                        .amount(amount)
                        .build())
                .post(Endpoint.DEPOSIT_USER.getUrl());

        if (response.statusCode() != 200) {
            throw new AssertionError("Unexpected status for deposit: " + response.statusCode()
                    + ", request={accountId=" + senderId + ", amount=" + amount + "}, body=" + response.asString());
        }

        return response.then();
    }

    public static ValidatableResponse depositMoneyWithInvalidData(String userToken, int senderId, float amount){
        return given()
                .spec(RequestSpec.userRequest(userToken))
                .body(UserDepositModelRequest.builder()
                        .accountId(senderId)
                        .amount(amount)
                        .build())
                .post(Endpoint.DEPOSIT_USER.getUrl())
                .then()
                .statusCode(400);
    }
}
