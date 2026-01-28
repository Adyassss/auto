package models;

import generators.GeneratingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}
