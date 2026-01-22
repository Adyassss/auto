package requests.skelethon.requests;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skelethon.Endpoint;
import requests.skelethon.HttpRequest;
import requests.skelethon.interfaces.CrudEndpointInterface;

public class ValidatedCrudRequester<T extends BaseModel> extends HttpRequest implements CrudEndpointInterface {
    private CrudRequesters crudRequesters;
    public ValidatedCrudRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
        this.crudRequesters = new CrudRequesters(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public T post(BaseModel baseModel) {
        return (T) crudRequesters.post(baseModel).extract().as(endpoint.getResponseModel());
    }

    @Override
    public T get() {
        return (T) crudRequesters.get().extract().as(endpoint.getResponseModel());
    }

    @Override
    public T put(BaseModel baseModel) {
        return (T) crudRequesters.put(baseModel).extract().as(endpoint.getResponseModel());
    }

    @Override
    public T delete(int id) {
        return (T) crudRequesters.delete(id).extract().as(endpoint.getResponseModel());
    }
}
