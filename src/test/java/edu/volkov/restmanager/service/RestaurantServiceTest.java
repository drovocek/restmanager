package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.MenuTestData.*;
import static edu.volkov.restmanager.RestaurantTestData.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    //USER TESTS
    @Test
    public void getAllWithOneDayMenu() {
        service.setDayMenuQuantity(1);
        List<Restaurant> restaurants = service.getAllWithPreassignedQuantityEnabledMenu();
        List<Menu> expectedDayMenus = getAllMenusFromRestaurants(restaurants);

        REST_MATCHER.assertMatch(restaurants, rest1, rest2, rest3, rest4, rest5);
        MENU_MATCHER.assertMatch(expectedDayMenus, todayActiveMenus);
    }

    @Test
    public void getAllWithTwoDaysMenu() {
        service.setDayMenuQuantity(2);
        List<Restaurant> restaurants = service.getAllWithPreassignedQuantityEnabledMenu();
        List<Menu> expectedDayMenus = getAllMenusFromRestaurants(restaurants);

        REST_MATCHER.assertMatch(restaurants, rest1, rest2, rest3, rest4, rest5);
        MENU_MATCHER.assertMatch(expectedDayMenus, allActiveMenus);
    }

    private List<Menu> getAllMenusFromRestaurants(List<Restaurant> restaurants) {
        return restaurants.stream()
                .flatMap(restaurant -> restaurant.getMenus().stream())
                .collect(Collectors.toList());
    }

    //ADMIN TESTS

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
    public void getByName() {
        Restaurant restaurant = service.getByName(rest1.getName());
        REST_MATCHER.assertMatch(restaurant, rest1);
    }

    @Test
    public void getAllWithoutMenu() {
        List<Restaurant> all = service.getAllWithoutMenu();
        all.forEach(System.out::println);
        REST_MATCHER.assertMatch(all, rest1, rest2, rest3, rest4, rest5);
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> service.create(new Restaurant(null, "  ", "testAddress", "+7 (903) 003-0303")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, "testName", "  ", "+7 (903) 003-0303")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, "testName", "testAddress", "BadNumber")), ConstraintViolationException.class);
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

    @Test
    public void votesQuantityTest() {
        assertEquals(service.get(rest1.getId()).getVotesQuantity(), rest1.getVotesQuantity());
        assertEquals(service.get(rest2.getId()).getVotesQuantity(), rest2.getVotesQuantity());
        assertEquals(service.get(rest3.getId()).getVotesQuantity(), rest3.getVotesQuantity());
        assertEquals(service.get(rest4.getId()).getVotesQuantity(), rest4.getVotesQuantity());
        assertEquals(service.get(rest5.getId()).getVotesQuantity(), rest5.getVotesQuantity());
    }

    @Test
    public void voteBeforeTimeLimit() {
        service.setChangeTimeLimit(LocalTime.now().plus(5, SECONDS));

        service.vote(0, rest1.getId(), LocalDate.now());
        long actualLikeAmount = rest1.getVotesQuantity() + 1;
        long expectedLikeAmount = service.get(rest1.getId()).getVotesQuantity();
        assertEquals(expectedLikeAmount, actualLikeAmount);
    }

    @Test
    public void voteDoubleBeforeTimeLimit() {
        service.setChangeTimeLimit(LocalTime.now().plus(5, SECONDS));
        LocalDate voteDate = LocalDate.now();

        service.vote(0, rest1.getId(), voteDate);
        long actualLikeAmount = rest1.getVotesQuantity() + 1;
        long expectedLikeAmount = service.get(rest1.getId()).getVotesQuantity();
        assertEquals(expectedLikeAmount, actualLikeAmount);

        service.vote(0, rest1.getId(), voteDate);
        actualLikeAmount = rest1.getVotesQuantity();
        expectedLikeAmount = service.get(rest1.getId()).getVotesQuantity();
        assertEquals(expectedLikeAmount, actualLikeAmount);
    }

    @Test
    public void voteAfterTimeLimit() {
        service.setChangeTimeLimit(LocalTime.now().minus(1, SECONDS));

        service.vote(0, rest1.getId(), LocalDate.now());
        long actualLikeAmount = rest1.getVotesQuantity() + 1;
        long expectedLikeAmount = service.get(rest1.getId()).getVotesQuantity();
        assertEquals(expectedLikeAmount, actualLikeAmount);
    }

    @Test
    public void voteDoubleAfterTimeLimit() {
        service.setChangeTimeLimit(LocalTime.now().minus(5, SECONDS));

        service.vote(0, rest1.getId(), LocalDate.now());
        long actualLikeAmount = rest1.getVotesQuantity() + 1;
        long expectedLikeAmount = service.get(rest1.getId()).getVotesQuantity();
        assertEquals(expectedLikeAmount, actualLikeAmount);

        service.vote(0, rest1.getId(), LocalDate.now());
        actualLikeAmount = rest1.getVotesQuantity() + 1;
        expectedLikeAmount = service.get(rest1.getId()).getVotesQuantity();
        assertEquals(expectedLikeAmount, actualLikeAmount);
    }

    public void createWithPhone(String phone, String name) {
        Restaurant newRest = getNew();
        newRest.setPhone(phone);
        newRest.setName(name);
        service.create(newRest);
    }
}