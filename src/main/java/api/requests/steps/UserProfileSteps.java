package api.requests.steps;

import api.models.AccountModel;
import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.models.UserProfileResponseModel;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.CrudRequesters;
import api.requests.skelethon.requests.ValidatedCrudRequester;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static UserProfileResponseModel getUserProfile(String userToken) {
        return new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .as(UserProfileResponseModel.class);
    }

    public static int createUserProfileId(String userToken) {
        return new ValidatedCrudRequester<CreateUserResponseModel>(RequestSpec.userRequest(userToken),
                Endpoint.USER_CREATE_ACC,
                ResponseSpec.created())
                .post(CreateUserRequestModel.builder().build())
                .getId();
    }

    public static float getUserProfileAccountBalance(String userToken, long accountId) {
        return (float) getProfileAccountBalance(userToken, accountId).getBalance();
    }

    public static AccountModel getProfileAccountBalance(String userToken, long accountId) {
        var body = new CrudRequesters(
                RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .asString();

        Pattern accountPattern = Pattern.compile(
                "\\{\\s*\"id\"\\s*:\\s*(\\d+)\\s*,\\s*\"accountNumber\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"balance\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)",
                Pattern.DOTALL
        );

        Matcher matcher = accountPattern.matcher(body);
        while (matcher.find()) {
            long id = Long.parseLong(matcher.group(1));
            if (id == accountId) {
                return AccountModel.builder()
                        .id(id)
                        .accountNumber(matcher.group(2))
                        .balance(Double.parseDouble(matcher.group(3)))
                        .build();
            }
        }

        throw new RuntimeException("Account with id " + accountId + " not found in profile response");
    }
}
