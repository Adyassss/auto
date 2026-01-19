package requests;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import models.UserCanTransferRequestModel;

import static io.restassured.RestAssured.given;

public class UserCanTransferRequest extends Request <UserCanTransferRequestModel> {


    public UserCanTransferRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(UserCanTransferRequestModel baseModel) {
        return given()
                .spec(requestSpecification)
                .body(baseModel)
                .post("/api/v1/accounts/transfer")
                .then()
                .spec(responseSpecification);
    }
}
