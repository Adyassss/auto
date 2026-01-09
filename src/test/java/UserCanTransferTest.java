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

public class UserCanTransferTest {
    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(), new ResponseLoggingFilter())
        );
    }

    // Positive cases
    @CsvSource({
            "1,2,50"
    })
    @ParameterizedTest
    public void userCanTransferMoney(String senderAccountId, String receiverAccountId, String amount) {
        String response = String.format("""
                {
                 "senderAccountId": "%s",
                 "receiverAccountId": "%s",
                 "amount": "%s"                 
                                  } 
                """, senderAccountId, receiverAccountId, amount);
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                .body(response)
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    //Negative cases
    @CsvSource({
            // Transfer money with an invalid token
            "1,2,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=",
            // Transfer money without a user token
            "1,2,50,Basic ",
            // Transfer money with an invalid sender account ID
            "200,2,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
            // Transfer money with an invalid receiver account ID
            "1,200,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
            // Transfer money with an invalid amount
            "1,2,-50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
            // Transfer money with insufficient funds
            "1,2,50000,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
            // Transfer money with zero amount
            "1,2,0,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
            // Transfer money with zero sender account ID
            "0,2,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
            // Transfer money with zero receiver account ID
            "1,0,50,Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==",
    })
    @ParameterizedTest
    public void userCantTransferMoneyWithInvalidData(String senderAccountId, String receiverAccountId, String amount, String token) {
        String response = String.format("""
                {
                 "senderAccountId": "%s",
                 "receiverAccountId": "%s",
                 "amount": "%s"                 
                                  } 
                """, senderAccountId, receiverAccountId, amount);
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(response)
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
