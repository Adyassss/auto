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
    DEPOSIT_USER(
                    "accounts/deposit",
                    UserDepositModelRequest.class,
                    UserDepositModelResponse.class
    ),
    TRANSFER_USER(
            "accounts/transfer",
            UserDepositModelRequest.class,
            UserDepositModelResponse.class
    ),
    CHANGE_NAME(
            "customer/profile",
            UserChangeNameResponseModel.class,
            UserChangeNameResponseModel.class
    ),
    USER_PROFILE(
            "customer/profile",
            UserProfileRequestModel.class,
            UserProfileResponseModel.class
    );



    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
