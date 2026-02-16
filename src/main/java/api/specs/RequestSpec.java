package api.specs;
import api.configs.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.List;

public class RequestSpec {
    private RequestSpec (){}
    @SuppressWarnings("null")
    public static RequestSpecBuilder defaultRequest (){
        return new RequestSpecBuilder()
                .setBaseUri(Config.getProperty("server") + Config.getProperty("apiVersion"))
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