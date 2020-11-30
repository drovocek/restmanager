package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.util.exception.NotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static edu.volkov.restmanager.testdata.MenuTestData.MENU_MATCHER;
import static edu.volkov.restmanager.testdata.MenuTestData.rest1EnabledMenus;
import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static org.junit.Assert.assertThrows;


public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    //USER
    @Test
    public void getWithDayEnabledMenu() {
        Restaurant actual = service.getWithDayEnabledMenu(0);
        REST_MATCHER.assertMatch(actual, rest1);
        MENU_MATCHER.assertMatch(actual.getMenus(), rest1EnabledMenus);
    }

    @Test
    public void getWithDayEnabledMenuNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithDayEnabledMenu(REST_NOT_FOUND_ID));
    }

    @Test
    public void getAllWithoutMenu() {
        List<Restaurant> actual = service.getAllWithoutMenu();
        REST_MATCHER.assertMatch(actual, rest1, rest2);
        assertThrows(LazyInitializationException.class, () -> actual.get(0).getMenus().get(0));
    }

    //ADMIN
    @Test
    public void create() {
        Restaurant created = service.create(getNew());
        int createdId = created.getId();
        Restaurant newRest = getNew();
        newRest.setId(createdId);
        REST_MATCHER.assertMatch(created, newRest);
        REST_MATCHER.assertMatch(service.get(createdId), newRest);
    }

    @Test
    public void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () -> service.create(duplicateNameRest));
    }

    @Test
    public void update() {
        service.update(getUpdated());
        REST_MATCHER.assertMatch(service.get(REST1_ID), getUpdated());
    }

    @Test
    public void delete() {
        service.delete(REST1_ID);
        assertThrows(NotFoundException.class, () -> service.get(REST1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(REST_NOT_FOUND_ID));
    }

    @Test
    public void get() {
        Restaurant dbRest = service.get(REST1_ID);
        REST_MATCHER.assertMatch(dbRest, rest1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(REST_NOT_FOUND_ID));
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> service.create(new Restaurant(null, "  ", "testAddress", "+7 (903) 003-0303", true, 0)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, "testName", "  ", "+7 (903) 003-0303", true, 0)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, "testName", "testAddress", "BadNumber", true, 0)), ConstraintViolationException.class);
    }

    @Test
    public void goodPhoneNumberTest() {
        createWithPhone("+7 (777) 777-7777", "testRest1");
        createWithPhone("+7(777)777-7777", "testRest2");
        createWithPhone("+7(777)7777777", "testRest3");
        createWithPhone("+77777777777", "testRest4");
        createWithPhone("+77777777777", "testRest5");
    }

    @Test
    public void badPhoneNumberTest() {
        validateRootCause(() -> createWithPhone("7 (777) 777-7777", "testRest1"), ConstraintViolationException.class);
        validateRootCause(() -> createWithPhone("+N (777) 777-7777", "testRest1"), ConstraintViolationException.class);
        validateRootCause(() -> createWithPhone("+7 (777) 777-77770", "testRest1"), ConstraintViolationException.class);
        validateRootCause(() -> createWithPhone("+7 (777) 777-777", "testRest1"), ConstraintViolationException.class);
    }

    public void createWithPhone(String phone, String name) {
        Restaurant newRest = getNew();
        newRest.setPhone(phone);
        newRest.setName(name);
        service.create(newRest);
    }
}