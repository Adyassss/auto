package api.requests.steps;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.models.UserProfileResponseModel;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.ValidatedCrudRequester;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;

public class UserProfileSteps extends BaseSteps {
    public UserProfileSteps(String username, String password) {
        super(username, password);
    }

    public static String getUserProfileName(String userToken) {
        return new ValidatedCrudRequester<UserProfileResponseModel>(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .getName();
    }
    public static int createUserProfileId(String userToken) {
        return new ValidatedCrudRequester<CreateUserResponseModel>(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel.builder().build())
                .getId();
    }
    public static float getUserProfileAccountBalance(String userToken, int accountId) {
        return new ValidatedCrudRequester<UserProfileResponseModel>(RequestSpec.userRequest(userToken),
        Endpoint.USER_PROFILE,
        ResponseSpec.ok())
        .get()
        .getAccounts()
        .stream()
        .filter(account -> account.getId() == accountId)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Account with id " + accountId + " not found"))
        .getBalance();
    }

    public static float getUserProfileAccountBalanceWithInvalidData(String userToken, int accountId) {
        return new ValidatedCrudRequester<UserProfileResponseModel>(RequestSpec.userRequest(userToken),
        Endpoint.USER_PROFILE,
        ResponseSpec.badRequest())
        .get()
        .getAccounts()
        .stream()
        .filter(account -> account.getId() == accountId)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Account with id " + accountId + " not found"))
        .getBalance();
    }

}
