package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class RequestSpec {
    public static RequestSpecBuilder defaultRequest (){
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:4111")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
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
}
