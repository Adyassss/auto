package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import models.UserProfileRequestModel;

import static io.restassured.RestAssured.given;

public class UserProfileRequest extends Request<UserProfileRequestModel> {

    public UserProfileRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(UserProfileRequestModel baseModel) {
        return   given()
                .spec(requestSpecification)
                .get("/api/v1/customer/profile")
                .then()
                .spec(responseSpecification);
    }
}
