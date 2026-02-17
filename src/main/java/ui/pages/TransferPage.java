package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import api.generators.RandomData;
import api.models.UserProfileResponseModel;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requests.ValidatedCrudRequester;
import api.specs.RequestSpec;
import api.specs.ResponseSpec;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class TransferPage extends BasePage<TransferPage> {
    
    private SelenideElement accountSelector = $("select.account-selector");
    private SelenideElement reciepAcc = $(Selectors.byAttribute("placeholder", "Enter recipient name"));
    private SelenideElement reciepAccNumber = $(Selectors.byAttribute("placeholder", "Enter recipient account number"));
    private SelenideElement amountInput = $(Selectors.byAttribute("placeholder", "Enter amount"));
    private SelenideElement confirmCheck = $(Selectors.byId("confirmCheck"));
    private SelenideElement buttonSendTransfer = $(Selectors.byText("\uD83D\uDE80 Send Transfer"));
    public String url() {
        return "/transfer";
    }

    public String getSecondAcc() {
        String token = Selenide.executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        );
        return new ValidatedCrudRequester<UserProfileResponseModel>(RequestSpec.userRequest(token), Endpoint.USER_PROFILE, ResponseSpec.ok())
                .get()
                .getAccounts()
                .stream()
                .skip(1)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Second account not found"))
                .getAccountNumber();
    }


    public TransferPage transferMoney(String name) {
        accountSelector.selectOption(1);
        reciepAcc.sendKeys(name);
        reciepAccNumber.sendKeys(getSecondAcc());
        amountInput.sendKeys("5000");
        confirmCheck.click();
        buttonSendTransfer.click();
        return this;
    }
  

    public TransferPage transferMoneyWithNotCorrectAmount(String name) {
        accountSelector.selectOption(1);
        reciepAcc.sendKeys(name);
        reciepAccNumber.sendKeys(getSecondAcc());
        amountInput.sendKeys("10001");
        confirmCheck.click();
        buttonSendTransfer.click();
        return this;
    }
}
