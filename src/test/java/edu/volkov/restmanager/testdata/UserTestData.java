package edu.volkov.restmanager.testdata;

import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.Collections;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER1_ID = 0;
    public static final int ADMIN_ID = 2;
    public static final int NOT_FOUND = 10;

    public static final User user1 = new User(USER1_ID, "User1", "user1@yandex.ru", "password1", Role.USER);
    public static final User user2 = new User(USER1_ID + 1, "User2", "user2@yandex.ru", "password2", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.USER, Role.ADMIN);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, LocalDateTime.now(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user1);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
