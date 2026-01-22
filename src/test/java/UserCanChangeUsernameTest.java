import generators.RandomData;
import models.UserChangeNameRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.skelethon.Endpoint;
import requests.skelethon.requests.CrudRequesters;
import requests.steps.AdminSteps;
import specs.RequestSpec;
import specs.ResponseSpec;

public class UserCanChangeUsernameTest extends BaseTest {

    @Test
    public void userCanChangeUsername() {
        String name = RandomData.getName();

        String userToken = AdminSteps.createUser();

        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.CHANGE_NAME,
                ResponseSpec.ok())
                .put(UserChangeNameRequestModel.builder()
                        .name(name)
                        .build());

        String nameAfter = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("name");

        softly.assertThat(nameAfter)
                .as("name")
                .isEqualTo(name);

        softly.assertAll();

    }

    @ParameterizedTest
    @MethodSource("generators.RandomData#NegativeNames")
    public void userCantChangeUsernameWithInvalidData(String invalidName) {
        String userToken = AdminSteps.createUser();

        String nameBefore = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("name");

        new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.CHANGE_NAME,
                ResponseSpec.badRequest())
                .put(UserChangeNameRequestModel.builder()
                        .name(invalidName)
                        .build());

        String nameAfter = new CrudRequesters(RequestSpec.userRequest(userToken),
                Endpoint.USER_PROFILE,
                ResponseSpec.ok())
                .get()
                .extract()
                .path("name");


        softly.assertThat(nameAfter)
                .as("name")
                .isEqualTo(nameBefore);

        softly.assertAll();
    }
}



