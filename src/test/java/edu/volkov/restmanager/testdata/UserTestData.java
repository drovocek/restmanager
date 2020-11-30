package edu.volkov.restmanager;

import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;

import java.util.Collections;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered");

    public static final int USER1_ID = 0;
    public static final int ADMIN_ID = 2;
    public static final int NOT_FOUND = 10;

    public static final User user1 = new User(USER1_ID, "User1", "user1@yandex.ru", "password1", Role.USER);
    public static final User user2 = new User(USER1_ID + 1, "User2", "user2@yandex.ru", "password2", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", Role.USER);
    }

    public static User getUpdated() {
        User updated = new User(admin);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }
}
