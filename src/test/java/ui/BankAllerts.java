package ui;

import lombok.Getter;

@Getter
public enum BankAllerts {
    USER_CREATED_SUCCESSFULLY("User created successfully!"),
    NEW_ACCOUNT_CREATED("New Account Created! Account Number:"),
    NAME_UPDATED_SUCCESSFULLY("Name updated successfully!"),
    NOT_CORRECT_NAME("Name must contain two words with letters only"),
    ENTER_VALID_NAME("❌ Please enter a valid name."),
    DEPOSIT_MONEY_SUCCESSFULLY("Successfully deposited $5000 to account"),
    ACCOUNT_CREATED_SUCCESSFULLY("New Account Created! Account Number:"),
    NOT_CORRECT_DEPOSIT_AMOUNT("❌ Please deposit less or equal to 5000$."),
    TRANSFER_MONEY_SUCCESSFULLY("✅ Successfully transferred");
    private final String message;

    BankAllerts(String message) {
        this.message = message;
    }

}
