package api.requests.skelethon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import api.models.*;

@Getter
@AllArgsConstructor
public enum Endpoint {
    ADMIN_USER(
            "admin/users",
            AdminCanCreateUserRequest.class,
            AdminCanCreateUserResponse.class
    ),
    USER_CREATE_ACC(
            "accounts",
            CreateUserRequestModel.class,
            CreateUserResponseModel.class
    ),
    LOGIN(
            "/auth/login",
            LoginUserRequest.class,
            LoginUserRequest.class
    ),
    DEPOSIT_USER(
                    "accounts/deposit",
                    UserDepositModelRequest.class,
                    UserProfileModelResponse.class
    ),
    TRANSFER_USER(
            "accounts/transfer",
            UserDepositModelRequest.class,
            UserProfileModelResponse.class
    ),
    CHANGE_NAME(
            "customer/profile",
            UserChangeNameResponseModel.class,
            UserChangeNameResponseModel.class
    ),
    USER_PROFILE(
            "customer/profile",
            BaseModel.class,
            UserProfileResponseModel.class
    ),
    TRANSFER_WITH_FRAUD_CHECK(
            "/accounts/transfer-with-fraud-check",
            TransferRequest.class,
            TransferResponse.class
    ),

    FRAUD_CHECK_STATUS(
            "/api/v1/accounts/fraud-check/{transactionId}",
            BaseModel.class,
            FraudCheckResponse.class
    );



    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
