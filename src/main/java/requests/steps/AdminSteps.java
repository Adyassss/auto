package requests.steps;

import generators.RandomModelGenerator;
import models.AdminCanCreateUserRequest;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import specs.RequestSpec;
import specs.ResponseSpec;

public class AdminSteps {
    public static String createUser (){
        AdminCanCreateUserRequest randomUser = RandomModelGenerator.generate(AdminCanCreateUserRequest.class);

        return new CrudRequesters(RequestSpec.adminRequest(),
                Endpoint.ADMIN_USER,
                ResponseSpec.created())
                .post(randomUser)
                .extract()
                .header("Authorization");
    }
}
