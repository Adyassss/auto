package api.models;

import api.configs.Config;
import api.generators.GeneratingRule;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AdminCanCreateUserRequest extends BaseModel {
    @GeneratingRule(regex = "^[A-Za-z0-9]{3,15}$")
    private String username;

    @GeneratingRule(regex = "^[A-Z]{5}[a-z]{5}\\d{3}[#\\$]{2}$")
    private String password;

    @GeneratingRule(regex = "USER")
    private String role;

    private String name;

    public static AdminCanCreateUserRequest getAdmin() {
        return AdminCanCreateUserRequest.builder().username(Config.getProperty(Config.ADMIN_USERNAME_KEY))
                .password(Config.getProperty(Config.ADMIN_PASSWORD_KEY)).build();
    }
}