package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponseModel extends BaseModel{
    private int id;
    private String accountNumber;
    private float balance;
    private String[] transactions;
}
