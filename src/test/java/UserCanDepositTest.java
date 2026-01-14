import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class UserCanDepositTest {
    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(), new ResponseLoggingFilter())
        );
    }

    @CsvSource({
            "testik,verysTRongPassword33$"
    })
    @ParameterizedTest
    public void authUser(String username, String password) {
        String requestBody = String.format("""
                {"username": "%s",
                 "password": "%s"}
                """, username, password);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:4111/api/v1/auth/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==");

    }

    //Positive cases
    @CsvSource({
            "1, 100.1",
            "1, 200.2"
    })
    @ParameterizedTest
    public void userCanDeposit(int id, String balance) {
        String requestBody = String.format("""
                {
                  "id": %d,
                  "balance": %s
                }
                """, id, balance);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                .body(requestBody)
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("transactions[-1].amount", equalTo(Float.parseFloat(balance)));
    }

    // Negative test cases
    @CsvSource({
            // Deposit money with an invalid user token and correct amount
            "1, 100, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Deposit money without a user token and correct amount
            "1, 100, Basic  , 401",
            // Deposit money with a valid user token and invalid amount
            "1, -100, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500",
            // Deposit money with a valid user token and without amount
            "1,  , Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500",
            // Deposit money with a valid user token and zero amount
            "1, 0, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500",
            // Deposit money with valid user token and amount exceeding maximum limit
            "1, 5000.01, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500",
            // Deposit money with valid user token and maximum allowed amount
            "1, 4999.99, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500",
            // Deposit money with valid user token and minimal positive amount
            "1, 0.01, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500"

    })

    @ParameterizedTest
    public void userCantDepositWithInvalidData(String id, String balance, String token, String error) {
        String requestBody = String.format("""
                {
                "id": "%s",
                "balance": "%s"}
                """, id, balance);
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(requestBody)
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .assertThat()
                .statusCode(Integer.parseInt(error));
    }
}

