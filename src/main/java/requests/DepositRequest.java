package requests;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import models.UserDepositModelRequest;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class DepositRequest extends Request <UserDepositModelRequest>{
    public DepositRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(UserDepositModelRequest baseModel) {
        return given()
                .spec(requestSpecification)
                .body(baseModel)
                .post("/api/v1/accounts/deposit")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
