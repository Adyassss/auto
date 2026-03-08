package common.extensions;

import api.configs.Config;
import common.annotations.AdminSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ui.pages.BasePage;

public class AdminSessionExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AdminSession annotation = context.getRequiredTestMethod().getAnnotation(AdminSession.class);
        if (annotation != null) {
            BasePage.authAsUser(Config.getProperty(Config.ADMIN_USERNAME_KEY),Config.getProperty(Config.ADMIN_PASSWORD_KEY));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
            SessionStorage.clear();
    }
}
