package api.specs;
import api.configs.Config;
import api.models.LoginUserRequest;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RequestSpec {
     static String authToken;
    private RequestSpec (){}
    @SuppressWarnings("null")
    public static RequestSpecBuilder defaultRequest (){
        return new RequestSpecBuilder()
                .setBaseUri(Config.getProperty("server") + Config.getProperty("apiVersion"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
    }
    public static RequestSpecification unauthSpec() {
        return defaultRequest().build();
    }

    public static RequestSpecification adminRequest(){
        return defaultRequest()
                .addHeader("Authorization","Basic YWRtaW46YWRtaW4=")
                .build();
    }

    public static RequestSpecification userRequest(String token){
        return defaultRequest()
                .addHeader("Authorization",token)
                .build();
    }


    public static void authAsUser(LoginUserRequest request) {
         authToken = given()
                .spec(defaultRequest().build())
                .body(request)
                .post("/auth/login")
                .then()
                 .spec(ResponseSpec.ok())
                .extract()
                .path("token");
    }


    public static String getUserAuthHeader(String username, String password) {
        String userAuthHeader;
            userAuthHeader = new CrudRequesters(
                    RequestSpec.unauthSpec(),
                    Endpoint.LOGIN,
                    ResponseSpec.ok())
                    .post(LoginUserRequest.builder().username(username).password(password).build())
                    .extract()
                    .header("Authorization");
            return userAuthHeader;
        }
    public static String getUserAuthHeader(LoginUserRequest user) {
        String userAuthHeader;
        userAuthHeader = new CrudRequesters(
                RequestSpec.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpec.ok())
                .post(user)
                .extract()
                .header("Authorization");
        return userAuthHeader;
    }
    }


