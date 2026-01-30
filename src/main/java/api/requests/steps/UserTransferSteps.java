package api.requests.steps;

import io.restassured.response.ValidatableResponse;
import api.models.UserCanTransferRequestModel;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;

public class UserTransferSteps {
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
}
