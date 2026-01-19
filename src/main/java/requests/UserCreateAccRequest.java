package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.CreateUserRequestModel;

import static io.restassured.RestAssured.given;

public class UserCreateAccRequest extends Request <CreateUserRequestModel>  {
    public UserCreateAccRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(CreateUserRequestModel baseModel) {
        return given()
                .spec(requestSpecification)
                .post("/api/v1/accounts")
                .then()
                .spec(responseSpecification);
    }
}
