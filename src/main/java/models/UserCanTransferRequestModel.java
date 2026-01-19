package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCanTransferRequestModel extends BaseModel {
    private int senderAccountId;
    private int receiverAccountId;
    private float amount;
}