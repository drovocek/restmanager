package edu.volkov.restmanager;

import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;

import java.time.LocalDate;
import java.util.EnumSet;

public class UserTestData {
    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator("registered");

    public static final int USER_ID = 0;
    public static final int ADMIN_ID = 1;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", true, EnumSet.of(Role.USER), LocalDate.now());
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", true, EnumSet.of(Role.USER, Role.ADMIN), LocalDate.now());

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", true, EnumSet.of(Role.USER, Role.ADMIN), LocalDate.now());
    }

    public static User getUpdated() {
        User updated = new User(admin);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(EnumSet.of(Role.ADMIN));
        updated.setRegistered(LocalDate.of(2020, 11, 17));
        return updated;
    }
}
