package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountModel {
    private int id;
    private String accountNumber;
    private float balance;
    private Object[] transactions;
}
