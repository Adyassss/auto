package specs;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class ResponseSpec {
    public static ResponseSpecBuilder defaultResponse (){
        return new ResponseSpecBuilder();
    }

    public static ResponseSpecification ok (){
        return defaultResponse()
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static ResponseSpecification created (){
        return defaultResponse()
                .expectStatusCode(HttpStatus.SC_CREATED)
                .build();
    }

    public static ResponseSpecification badRequest (){
        return defaultResponse()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }

    public static ResponseSpecification unauthorized (){
        return defaultResponse()
                .expectStatusCode(HttpStatus.SC_UNAUTHORIZED)
                .build();
    }
}
