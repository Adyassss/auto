package api.requests.steps;

import io.restassured.response.ValidatableResponse;
import api.models.UserDepositModelRequest;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;

public class UserDepositSteps extends BaseSteps {
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
