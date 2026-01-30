package api.specs;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpec {
    private static final int SC_OK = 200;
    private static final int SC_CREATED = 201;
    private static final int SC_BAD_REQUEST = 400;
    private static final int SC_UNAUTHORIZED = 401;

    public static ResponseSpecBuilder defaultResponse (){
        return new ResponseSpecBuilder();
    }

    public static ResponseSpecification ok (){
        return defaultResponse()
                .expectStatusCode(SC_OK)
                .build();
    }

    public static ResponseSpecification created (){
        return defaultResponse()
                .expectStatusCode(SC_CREATED)
                .build();
    }

    public static ResponseSpecification badRequest (){
        return defaultResponse()
                .expectStatusCode(SC_BAD_REQUEST)
                .build();
    }

    public static ResponseSpecification unauthorized (){
        return defaultResponse()
                .expectStatusCode(SC_UNAUTHORIZED)
                .build();
    }
}
