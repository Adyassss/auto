package requests.steps;
import io.restassured.response.ValidatableResponse;
import models.UserChangeNameRequestModel;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import specs.RequestSpec;
import specs.ResponseSpec;


public class ChangeNameSteps {
    public static ValidatableResponse changeName(String userToken, String name) {
        return new CrudRequesters(
            RequestSpec.userRequest(userToken),
            Endpoint.CHANGE_NAME,
            ResponseSpec.ok())
            .put(UserChangeNameRequestModel.builder().name(name).build());
    }

    public static ValidatableResponse changeNameWithInvalidData(String userToken, String name) {
        return new CrudRequesters(
            RequestSpec.userRequest(userToken),
            Endpoint.CHANGE_NAME,
            ResponseSpec.badRequest())
            .put(UserChangeNameRequestModel.builder().name(name).build());
    }

}
