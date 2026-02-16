
package ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public class UserElement extends BaseElement {
    private String username;
    private String role;

    public UserElement(SelenideElement element) {
        super(element);
        username = element.getText().split("\n")[0];
        role = element.getText().split("\n")[1];
    }
}
