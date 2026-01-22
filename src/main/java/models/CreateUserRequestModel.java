package models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequestModel extends BaseModel {
    private String dummy = "";
}
