package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class BasePage<T extends BasePage<T>> {
    public abstract String url();
    public static String secondAcc = "";
    protected SelenideElement usernameInput = $(Selectors.byAttribute("placeholder", "Username"));
    protected SelenideElement passwordInput = $(Selectors.byAttribute("placeholder", "Password"));
    protected SelenideElement loginButton = $("button");

    public T open(){
       return (T) Selenide.open(url(), (Class<T>) this.getClass());
    }
    public <P extends BasePage<P>> P getPage(Class<P> pageClass){
        return Selenide.page(pageClass);
    }

    @SuppressWarnings("unchecked")
    public T checkAllertMassageAndAccept(String expectedMessage) {
        int count = 0;
        if (expectedMessage.contains("New Account Created! Account Number:"))
        {
            count++;
        }
        if (count >= 1) {
            String[] filter = expectedMessage.split(" ");
            secondAcc = filter[filter.length - 1];
        }
        var alert = switchTo().alert();
        assertThat(alert.getText()).contains(expectedMessage);
        alert.accept();
        return (T) this;
    }
}
