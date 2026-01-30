package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;

public class UserDashboard extends BasePage<UserDashboard> {
    private SelenideElement welcomeText = $(Selectors.byText("User Dashboard"));
    private SelenideElement oldName = $(Selectors.byText("Noname"));
    private SelenideElement newName = $(Selectors.byAttribute("placeholder", "Enter new name"));
    private SelenideElement buttonChangeName = $(Selectors.byText("\uD83D\uDCBE Save Changes"));
    private SelenideElement createAccountButton = $(Selectors.byText("➕ Create New Account"));
    private SelenideElement transferButton = $(Selectors.byText("\uD83D\uDCB0 Deposit Money"));
    @Override
    public String url() {
        return "/dashboard";
    }

    public UserDashboard ensureDashboardVisible() {
        welcomeText.shouldBe(Condition.visible);
        return this;
    }

    public UserDashboard changeName(String name) {
        oldName.shouldBe(Condition.visible).click();
        newName.sendKeys(name);
        buttonChangeName.shouldBe(Condition.visible).click();
        return this;
    }

    public UserDashboard createAccount() {
        createAccountButton.shouldBe(Condition.visible).click();
        return this;
    }

    public UserDashboard transferMoney() {
        transferButton.shouldBe(Condition.visible).click();
        return this;
    }

}
