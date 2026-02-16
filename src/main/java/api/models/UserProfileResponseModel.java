package api.models;

import lombok.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseModel extends BaseModel {

    private int id;
    private String username;
    private String password;
    private String name;
    private String role;

    private List<AccountModel> accounts;
}
