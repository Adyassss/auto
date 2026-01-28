package requests.steps;

import io.restassured.response.ValidatableResponse;
import models.UserDepositModelRequest;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import specs.RequestSpec;
import specs.ResponseSpec;

public class UserDepositSteps {
    public static ValidatableResponse depositMoney(String userToken, int senderId, float amount){
        return new CrudRequesters(RequestSpec.userRequest(userToken),
        Endpoint.DEPOSIT_USER,
        ResponseSpec.ok())
        .post(UserDepositModelRequest.builder()
                .id(senderId)
                .balance(amount)
                .build());
    }

    public static ValidatableResponse depositMoneyWithInvalidData(String userToken, int senderId, float amount){
        return new CrudRequesters(RequestSpec.userRequest(userToken),
        Endpoint.DEPOSIT_USER,
        ResponseSpec.badRequest())
        .post(UserDepositModelRequest.builder()
                .id(senderId)
                .balance(amount)
                .build());
    }

}
