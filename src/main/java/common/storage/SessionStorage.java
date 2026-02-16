package common.storage;

import api.models.BaseModel;
import api.requests.steps.BaseSteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SessionStorage {
    private static final SessionStorage INSTANCE = new SessionStorage();
    private final LinkedHashMap<BaseModel, BaseSteps> userStepsMap = new LinkedHashMap<>();
    private SessionStorage() {}
    public static void addUsers(BaseModel model, BaseSteps step) {
       INSTANCE.userStepsMap.put(model,step);
    }
    public static BaseModel getUser(int number) {
        return new ArrayList<>(INSTANCE.userStepsMap.keySet()).get(number-1);
    }

    public static BaseModel getUser() {
        return getUser(1);
    }

    public static BaseSteps getSteps(int number) {
        return new ArrayList<>(INSTANCE.userStepsMap.values()).get(number-1);
    }

    public static BaseSteps getSteps() {
        return getSteps(1);
    }

    public static void clear() {
        INSTANCE.userStepsMap.clear();
    }
}
