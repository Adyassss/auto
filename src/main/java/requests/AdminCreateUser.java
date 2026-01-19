package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.AdminCanCreateUserRequest;


import static io.restassured.RestAssured.given;

public class AdminCreateUser extends Request<AdminCanCreateUserRequest> {
    public AdminCreateUser(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(AdminCanCreateUserRequest baseModel) {
        return given()
                .spec(requestSpecification)
                .body(baseModel)
                .post("/api/v1/admin/users")
                .then()
                .assertThat()
                .spec(responseSpecification);

    }
}
