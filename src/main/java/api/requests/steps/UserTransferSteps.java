package api.requests.steps;

import api.models.TransferRequest;
import api.models.TransferResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import api.models.UserCanTransferRequestModel;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import static io.restassured.RestAssured.given;
import api.specs.ResponseSpec;

public class UserTransferSteps extends BaseSteps {
    public UserTransferSteps(String username, String password) {
        super(username, password);
    }

    public static ValidatableResponse transferMoney(String userToken, int senderId, int receiverId, float amount){
        return new CrudRequesters(RequestSpec.userRequest(userToken),
        Endpoint.TRANSFER_USER,
        ResponseSpec.ok())
        .post(UserCanTransferRequestModel.builder()
                .senderAccountId(senderId)
                .receiverAccountId(receiverId)
                .amount(amount)
                .build());
    }

    public static ValidatableResponse transferMoneyWithInvalidData(String userToken, int senderId, int receiverId, float amount){
        return new CrudRequesters(RequestSpec.userRequest(userToken),
        Endpoint.TRANSFER_USER,
        ResponseSpec.badRequest())
        .post(UserCanTransferRequestModel.builder()
                .senderAccountId(senderId)
                .receiverAccountId(receiverId)
                .amount(amount)
                .build());
    }

    public static TransferResponse transferWithFraudCheck(String userToken, int senderId, int receiverId, float amount) {
        Response response = given()
                .spec(RequestSpec.userRequest(userToken))
                .body(TransferRequest.builder()
                        .senderAccountId((long) senderId)
                        .receiverAccountId((long) receiverId)
                        .amount(amount)
                        .description("Test transfer with fraud check")
                        .build())
                .post(Endpoint.TRANSFER_WITH_FRAUD_CHECK.getUrl());

        if (response.statusCode() == 200) {
            return response.as(TransferResponse.class);
        }

        // Fallback for backend variants that accept the old transfer payload without description.
        Response fallback = given()
                .spec(RequestSpec.userRequest(userToken))
                .body(UserCanTransferRequestModel.builder()
                        .senderAccountId(senderId)
                        .receiverAccountId(receiverId)
                        .amount(amount)
                        .build())
                .post(Endpoint.TRANSFER_WITH_FRAUD_CHECK.getUrl());

        if (fallback.statusCode() == 200) {
            return fallback.as(TransferResponse.class);
        }

        throw new AssertionError("Unexpected status for transfer-with-fraud-check: " + response.statusCode()
                + ", body=" + response.asString()
                + "; fallbackStatus=" + fallback.statusCode()
                + ", fallbackBody=" + fallback.asString());
    }

}
