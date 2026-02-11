package api.models;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionModel {
    private Integer id;
    private Float amount;
    private String type;
    private String timestamp;
    private Integer relatedAccountId;
}
