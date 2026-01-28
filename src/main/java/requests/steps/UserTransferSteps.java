package requests.steps;

import io.restassured.response.ValidatableResponse;
import models.UserCanTransferRequestModel;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import specs.RequestSpec;
import specs.ResponseSpec;

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
