package api.models;

import lombok.Data;
import java.util.List;

@Data
public class AccountResponse {
    private Integer id;
    private String accountNumber;
    private Float balance;
    private List<TransactionModel> transactions;
}