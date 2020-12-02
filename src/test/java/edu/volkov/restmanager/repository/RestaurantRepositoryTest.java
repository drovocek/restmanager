package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.AbstractTest;
import edu.volkov.restmanager.model.AbstractBaseEntity;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.testdata.MenuTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.getNew;
import static edu.volkov.restmanager.testdata.RestaurantTestData.getUpdated;
import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static org.junit.Assert.*;


public class RestaurantRepositoryTest extends AbstractTest {

    @Autowired
    private RestaurantRepository repository;

    @Test
    public void getWithDayEnabledMenu() {
        Restaurant actual = repository.getWithDayEnabledMenu(REST1_ID);
        REST_MATCHER.assertMatch(actual, rest1);
        MENU_MATCHER.assertMatch(actual.getMenus(), rest1DayEnabledMenus);
    }

    @Test
    public void getWithDayEnabledMenuNotFound() {
        assertNull(repository.getWithDayEnabledMenu(REST_NOT_FOUND_ID));
    }

    @Test
    public void getAllWithDayEnabledMenu() {
        List<Restaurant> actualRests = repository.getAllWithDayEnabledMenu();
        REST_MATCHER.assertMatch(actualRests, rest1, rest2);
        List<Menu> actualMenus = extractMenuOrderById(actualRests);
        MENU_MATCHER.assertMatch(actualMenus, allDayEnabledMenus);
    }

    @Test
    public void create() {
        Restaurant created = repository.save(getNew());
        int createdId = created.getId();
        Restaurant newRest = getNew();
        newRest.setId(createdId);
        REST_MATCHER.assertMatch(created, newRest);
        REST_MATCHER.assertMatch(repository.get(createdId), newRest);
    }

    @Test
    public void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () -> repository.save(duplicateNameRest));
    }

    @Test
    public void update() {
        repository.save(getUpdated());
        REST_MATCHER.assertMatch(repository.get(REST1_ID), getUpdated());
    }

    @Test
    public void delete() {
        repository.delete(REST1_ID);
        assertNull(repository.get(REST1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertFalse(repository.delete(REST_NOT_FOUND_ID));
    }

    @Test
    public void get() {
        Restaurant dbRest = repository.get(REST1_ID);
        REST_MATCHER.assertMatch(dbRest, rest1);
    }

    @Test
    public void getNotFound() {
        assertNull(repository.get(REST_NOT_FOUND_ID));
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> repository.save(new Restaurant(null, "  ", "testAddress", "+7 (903) 003-0303", true, 0)), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Restaurant(null, "testName", "  ", "+7 (903) 003-0303", true, 0)), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Restaurant(null, "testName", "testAddress", "BadNumber", true, 0)), ConstraintViolationException.class);
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
        repository.save(newRest);
    }

    private List<Menu> extractMenuOrderById(List<Restaurant> rests) {
        Comparator<Menu> byId = Comparator.comparing(AbstractBaseEntity::getId);
        return rests.stream()
                .flatMap(menu -> menu.getMenus().stream())
                .sorted(byId)
                .collect(Collectors.toList());
    }
}