package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

import com.codeborne.selenide.Condition;

public class DepositPage extends BasePage<DepositPage> {

    public static final float CORRECT_DEPOSIT_AMOUNT = 5000f;
    public static final float INVALID_DEPOSIT_AMOUNT = 5001f;
    public static final float ZERO_BALANCE = 0f;
    public static final float AMOUNT_TOLERANCE = 0.01f;

    private SelenideElement accountSelector = $("select");
    private SelenideElement amountInput = $(Selectors.byAttribute("placeholder", "Enter amount"));
    private SelenideElement buttonDeposit = $(Selectors.byText("\uD83D\uDCB5 Deposit"));
    public final SelenideElement depositText = $(Selectors.byText("💰 Deposit Money"));

    @Override
    public String url() {
        return "/deposit";
    }

    public DepositPage ensureDepositPageVisible() {
        depositText.shouldBe(Condition.visible);
        return this;
    }

        public DepositPage depositMoney() {
            accountSelector.selectOption(1);
            amountInput.sendKeys(String.valueOf(CORRECT_DEPOSIT_AMOUNT));
            buttonDeposit.click();
            return this;
        }
    public DepositPage depositMoneyWithInvalidAmount() {
        sleep(500);
        accountSelector.selectOption(1);
        amountInput.sendKeys(String.valueOf(INVALID_DEPOSIT_AMOUNT));
        buttonDeposit.click();
        return this;
    }
}
