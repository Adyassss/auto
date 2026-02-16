package api.models;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class CreateUserRequestModel extends BaseModel {
    @Builder.Default
    private String dummy = "";
}
