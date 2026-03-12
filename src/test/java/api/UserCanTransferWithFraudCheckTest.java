package api;

import api.generators.RandomData;
import api.models.TransferResponse;
import api.requests.steps.AdminSteps;
import api.requests.steps.UserDepositSteps;
import api.requests.steps.UserProfileSteps;
import api.requests.steps.UserTransferSteps;
import common.annotations.FraudCheckMock;
import common.extensions.FraudCheckWireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(FraudCheckWireMockExtension.class)
public class UserCanTransferWithFraudCheckTest {

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCanTransferWithFraudCheck() {
        float amount = RandomData.getAmount();
        String userToken = AdminSteps.createToken();

        int senderId = UserProfileSteps.createUserProfileId(userToken);
        UserDepositSteps.depositMoney(userToken, senderId, amount);
        int receiverId = UserProfileSteps.createUserProfileId(userToken);

        TransferResponse response = TransferResponse.builder()
                .status("APPROVED")
                .senderAccountId((long) senderId)
                .receiverAccountId((long) receiverId)
                .amount(amount)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("APPROVED");
        assertThat(response.getSenderAccountId()).isEqualTo((long) senderId);
        assertThat(response.getReceiverAccountId()).isEqualTo((long) receiverId);
        assertThat(response.getAmount()).isEqualTo(amount);
    }
}
