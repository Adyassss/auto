import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCanTransferTest {
    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(), new ResponseLoggingFilter())
        );
    }

    // Positive cases
    @CsvSource({
            "1,2,50.2",
            "1,2,300.2"
    })
    @ParameterizedTest
    public void userCanTransferMoney(int senderAccountId, int receiverAccountId, float amount) {
        String response = String.format("""
                {
                 "senderAccountId": %d,
                 "receiverAccountId": %d,
                 "amount": %s                 
                                  } 
                """, senderAccountId, receiverAccountId, amount);
        float beforeBalance =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                .body(response)
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Transfer successful"))
                .body("amount", equalTo(amount));

        float afterBalance =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");

        float expectedAfter = beforeBalance + amount;
        assertEquals(expectedAfter, afterBalance, 0.001);

    }

    //Negative cases
    @CsvSource({
            // Transfer money with an invalid token
            "1,2,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=,401",
            // Transfer money without a user token
            "1,2,50,Basic ,401",
            // Transfer money with an invalid sender account ID
            "200,2,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 403",
            // Transfer money with an invalid receiver account ID
            "1,200,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 400",
            // Transfer money with an invalid amount
            "1,2,-50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 400",
            // Transfer money with insufficient funds
            "1,2,50000,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 400",
            // Transfer money with zero amount
            "1,2,0,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 400",
            // Transfer money with zero sender account ID
            "0,2,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==, 403",
            // Transfer money with zero receiver account ID
            "1,0,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==,400",
            // Transfer money with amount exceeding available balance
            "2,1,10000.01,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==,400",
            // Transfer money with maximum valid amount
            "2,1,9999.99,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==,400"


    })
    @ParameterizedTest
    public void userCantTransferMoneyWithInvalidData(int senderAccountId, int receiverAccountId, String amount, String token, String error) {
        String response = String.format("""
                {
                 "senderAccountId": %d,
                 "receiverAccountId": %d,
                 "amount": %s                 
                                  } 
                """, senderAccountId, receiverAccountId, amount);
        float beforeBalance =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                        .get("http://localhost:4111/api/v1/customer/profile")
                        .then()
                        .extract()
                        .path("accounts[0].balance");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(response)
                .post("http://localhost:4111/api/v1/accounts/transfer")
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
