package common.storage;

import api.models.BaseModel;
import api.requests.steps.BaseSteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class SessionStorage {
    private static final ThreadLocal<SessionStorage> INSTANCE = ThreadLocal.withInitial(SessionStorage::new);
    private final LinkedList<BaseModel> usersStorage = new LinkedList<>();
    private SessionStorage() {}
    public static void addUsers(BaseModel model) {
       INSTANCE.get().usersStorage.add(model);
    }
    public static BaseModel getUser(int number) {
        return INSTANCE.get().usersStorage.get(number-1);
    }

    public static BaseModel getUser() {
        return getUser(1);
    }

    public static void clear() {
        INSTANCE.get().usersStorage.clear();
    }
}
