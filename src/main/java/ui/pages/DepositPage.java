package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;

public class DepositPage extends BasePage<DepositPage> {
    
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
            amountInput.sendKeys("5000");
            buttonDeposit.click();
            return this;
        }
    public DepositPage depositMoneyWithInvalidAmount() {
        accountSelector.selectOption(1);
        amountInput.sendKeys("5001");
        buttonDeposit.click();
        return this;
    }
}
