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

public class UserCanChangeUsernameTest {
    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(), new ResponseLoggingFilter())
        );
    }

    // Positive Cases
    @CsvSource({
            "vasya pupkin",
            "petya gromov",
            "masha lom"
    })

    @ParameterizedTest
    public void userCanChangeUsername(String name) {
        String request = String.format("""
                {
                    "name": "%s"
                }
                """, name);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA==")
                .body(request)
                .put("http://localhost:4111/api/v1/customer/profile")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Profile updated successfully"));
    }

    // Negative Cases
    @CsvSource({
            // Update username with USER role and an invalid token
            "vasya, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Update username with USER role and without a token
            "vasya, Basic ,401 ",
            // Update username with USER role and empty username
            " , Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Update username with USER role and username shorter than 3 characters
            "va, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Update username with USER role and username longer than 15 characters
            "aaaaaaaaaaaaaaaa, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Update username with USER role and special characters in username
            "adyasss!/.@#$&*)({}, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Update username with USER role and the same username
            "adya1997, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401",
            // Update username with USER role and spaces in username
            "adya 1997, Basic dGVzdGlrOnZlcnlzVFJvbmdQYXNzd29yZDMzJA=, 401"


    })

    @ParameterizedTest
    public void userCantChangeUsernameWithInvalidData(String name, String token, String error) {
        String request = String.format("""
                {
                    "name": "%s"
                }
                """, name);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .body(request)
                .put("http://localhost:4111/api/v1/customer/profile")
                .then()
                .assertThat()
                .statusCode(Integer.parseInt(error));
    }
}
