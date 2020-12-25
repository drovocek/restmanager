package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.EnumSet;
import java.util.List;

import static edu.volkov.restmanager.testdata.UserTestData.*;
import static org.junit.Assert.assertThrows;

public class UserServiceTest extends AbstractTest {

    @Autowired
    protected UserService service;

    @Test
    public void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user1@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        service.delete(ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_NOT_FOUND));
    }

    @Test
    public void get() {
        User user = service.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(USER_NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER1_ID), getUpdated());
    }

    @Test
    public void updateWithoutRoles() {
        User newWithoutRoles = getNew();
        newWithoutRoles.setRoles(EnumSet.noneOf(Role.class));
        int newId = service.create(newWithoutRoles).getId();

        User updatedWithRoles = getUpdated();
        updatedWithRoles.setId(newId);
        service.update(updatedWithRoles);

        User updatedWithRolesForMatching = getUpdated();
        updatedWithRolesForMatching.setId(newId);

        USER_MATCHER.assertMatch(service.get(newId), updatedWithRolesForMatching);
    }

    @Test
    public void updateDeleteAllRoles() {
        User updatedWithoutRoles1 = getUpdated();
        updatedWithoutRoles1.setRoles(EnumSet.noneOf(Role.class));
        service.update(updatedWithoutRoles1);

        User updatedWithoutRoles2 = getUpdated();
        updatedWithoutRoles2.setRoles(EnumSet.noneOf(Role.class));

        USER_MATCHER.assertMatch(service.get(USER1_ID), updatedWithoutRoles2);
    }

    @Test
    public void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, user1, user2);
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)), ConstraintViolationException.class);
    }
}