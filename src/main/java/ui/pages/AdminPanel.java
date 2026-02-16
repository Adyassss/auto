package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import lombok.Getter;

@Getter
public class AdminPanel extends BasePage<AdminPanel> {
    private SelenideElement username = $(Selectors.byAttribute("placeholder", "Username"));
    private SelenideElement password = $(Selectors.byAttribute("placeholder", "Password"));
    private SelenideElement createButton = $(".btn.btn-primary.w-100");
    private SelenideElement logoutButton = $(".btn.btn-danger");
    public final SelenideElement adminPanelText = $(Selectors.byText("Admin Panel"));

    @Override
    public String url() {
        return "/admin";
    }

    public AdminPanel ensureAdminPanelVisible() {
        adminPanelText.shouldBe(Condition.visible);
        return this;
    }

    public AdminPanel createUser(String usernamee, String passwordd) {
        username.sendKeys(usernamee);
        password.sendKeys(passwordd);
        createButton.click();
        return this;
    }

    public AdminPanel logout() {
        this.logoutButton.click();
        return this;
    }

}
