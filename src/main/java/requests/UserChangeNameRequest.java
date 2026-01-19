package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.UserChangeNameRequestModel;

import static io.restassured.RestAssured.given;

public class UserChangeNameRequest extends Request <UserChangeNameRequestModel>{
    public UserChangeNameRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(UserChangeNameRequestModel baseModel) {
        return given()
                .spec(requestSpecification)
                .body(baseModel)
                .put("/api/v1/customer/profile")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
