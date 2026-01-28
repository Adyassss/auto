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
public class UserCanTransferResponseModel extends BaseModel {
    private int receiverAccountId;
    private float amount;
    private String message;
    private int senderAccountId;
}