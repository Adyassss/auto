package api.requests.steps;
import io.restassured.response.ValidatableResponse;
import api.models.UserChangeNameRequestModel;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;


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
