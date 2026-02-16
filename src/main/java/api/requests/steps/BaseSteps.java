package api.requests.steps;

import api.models.BaseModel;
import api.models.LoginUserRequest;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public abstract class BaseSteps {
    private String username;
    private String password;

    protected BaseModel authSpec(String username,String password) {
        return new CrudRequesters(
                RequestSpec.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpec.ok())
                .post(LoginUserRequest.builder().username(username).password(password).build())
                .extract()
                .as(BaseModel.class);
    }
}
