package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseModel extends BaseModel{
    private int id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String [] accounts;
}
