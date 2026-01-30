package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import api.generators.RandomData;
import ui.BaseUITest;


import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;

public class TransferPage extends BasePage<TransferPage> {
    
    private SelenideElement accountSelector = $("select.account-selector");
    private SelenideElement reciepAcc = $(Selectors.byAttribute("placeholder", "Enter recipient name"));
    private SelenideElement reciepAccNumber = $(Selectors.byAttribute("placeholder", "Enter recipient account number"));
    private SelenideElement amountInput = $(Selectors.byAttribute("placeholder", "Enter amount"));
    private SelenideElement confirmCheck = $(Selectors.byId("confirmCheck"));
    private SelenideElement buttonSendTransfer = $(Selectors.byText("\uD83D\uDE80 Send Transfer"));
    String newName = RandomData.getName();
    public String url() {
        return "/transfer";
    }


    public TransferPage transferMoney() {
        accountSelector.selectOption(1);
        reciepAcc.sendKeys(newName);
        reciepAccNumber.sendKeys(BaseUITest.secondAcc);
        amountInput.sendKeys("10001");
        confirmCheck.click();
        buttonSendTransfer.click();
        return this;
    }
}
