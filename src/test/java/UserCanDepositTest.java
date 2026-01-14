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
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            "2, 100.1",
            "2, 200.2"
    })
    @ParameterizedTest
    public void userCanDeposit(int id, float balance) {
        String requestBody = String.format("""
                {
                  "id": %d,
                  "balance": %s
                }
                """, id, balance);
        float beforeBalance =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                .body(requestBody)
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        float afterBalance =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");

        float totalBalance = beforeBalance + balance;
        assertEquals(totalBalance, afterBalance, 0.001);
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
            "1, 0 , Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 500",
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
    public void userCantDepositWithInvalidData(String id, float balance, String token, String error) {
        String requestBody = String.format("""
                {
                "id": "%s",
                "balance": "%s"}
                """, id, balance);

        float beforeBalance =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");


        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(requestBody)
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .assertThat()
                .statusCode(Integer.parseInt(error));

        float afterBalance =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");


        assertEquals(beforeBalance, afterBalance, 0.001);
    }
}

