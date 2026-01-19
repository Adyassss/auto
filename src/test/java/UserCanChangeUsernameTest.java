import generators.RandomData;
import models.AdminCanCreateUserRequest;
import models.UserChangeNameRequestModel;
import models.UserProfileRequestModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.AdminCreateUser;
import requests.UserChangeNameRequest;
import requests.UserProfileRequest;
import specs.RequestSpec;
import specs.ResponseSpec;

public class UserCanChangeUsernameTest extends BaseTest {

    @Test
    public void userCanChangeUsername() {
        String name = RandomData.getName();

        String userToken = new AdminCreateUser(RequestSpec.adminRequest(), ResponseSpec.created())
                .post(AdminCanCreateUserRequest.builder()
                        .username(RandomData.getUsername())
                        .password(RandomData.getPassword())
                        .role(UserRole.USER.toString())
                        .build())
                .extract()
                .header("Authorization");

        new UserChangeNameRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(UserChangeNameRequestModel.builder()
                        .name(name)
                        .build());

        String nameAfter = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
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
        String userToken = new AdminCreateUser(RequestSpec.adminRequest(), ResponseSpec.created())
                .post(AdminCanCreateUserRequest.builder()
                        .username(RandomData.getUsername())
                        .password(RandomData.getPassword())
                        .role(UserRole.USER.toString())
                        .build())
                .extract()
                .header("Authorization");

        String nameBefore = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("name");

        new UserChangeNameRequest(RequestSpec.userRequest(userToken), ResponseSpec.badRequest())
                .post(UserChangeNameRequestModel.builder()
                        .name(invalidName)
                        .build());

        String nameAfter = new UserProfileRequest(RequestSpec.userRequest(userToken), ResponseSpec.ok())
                .post(new UserProfileRequestModel())
                .extract()
                .path("name");


        softly.assertThat(nameAfter)
                .as("name")
                .isEqualTo(nameBefore);

        softly.assertAll();
    }
}


