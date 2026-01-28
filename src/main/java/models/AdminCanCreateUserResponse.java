package models;

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
public class AdminCanCreateUserResponse extends BaseModel {
    private int id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String[] accounts;
}
