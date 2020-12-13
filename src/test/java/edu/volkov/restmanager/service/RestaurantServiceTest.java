package edu.volkov.restmanager.service;

import edu.volkov.restmanager.AbstractTest;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.testdata.MenuTestData;
import edu.volkov.restmanager.to.RestaurantTo;
import edu.volkov.restmanager.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static edu.volkov.restmanager.util.model.RestaurantUtil.asTo;
import static org.junit.Assert.*;

public class RestaurantServiceTest extends AbstractTest {

    @Autowired
    protected RestaurantService service;

    @Test
    public void create() {
        Restaurant created = service.create(getNew());
        int newId = created.id();
        Restaurant newRest = getNew();
        newRest.setId(newId);
        REST_MATCHER.assertMatch(created, newRest);
        REST_MATCHER.assertMatch(service.getWithoutMenu(newId), newRest);
    }

    @Test
    public void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Restaurant(null, "rest1", "newAddress", "+7 (977) 777-7777", true, 0)));
    }

    @Test
    public void delete() {
        service.delete(REST1_ID);
        assertThrows(NotFoundException.class, () -> service.getWithoutMenu(REST1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(REST_NOT_FOUND_ID));
    }

    @Test
    public void getWithoutMenu() {
        Restaurant rest = service.getWithoutMenu(REST1_ID);
        REST_MATCHER.assertMatch(rest, rest1);
    }

    @Test
    public void getWithoutMenuNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithoutMenu(REST_NOT_FOUND_ID));
    }

    @Test
    public void getWithEnabledMenu() {
        service.setTestDate(MenuTestData.TODAY);
        Restaurant rest = service.getWithEnabledMenu(REST1_ID);
        REST_WITH_MENU_MATCHER.assertMatch(rest, rest1WithDayEnabledMenusAndItems);
    }

    @Test
    public void getAllWithDayEnabledMenu() {
        service.setTestDate(MenuTestData.TODAY);
        List<Restaurant> rests = service.getAllWithDayEnabledMenu();
        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems, rest2WithDayEnabledMenusAndItems);
    }

    @Test
    public void getAllEnabledWithDayEnabledMenu() {
        service.setTestDate(MenuTestData.TODAY);
        List<Restaurant> rests = service.getAllEnabledWithDayEnabledMenu();
        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems);
    }

    @Test
    public void getAllFilteredWithDayEnabledMenu() {
        service.setTestDate(MenuTestData.TODAY);
        List<Restaurant> rests = service.getFilteredWithDayEnabledMenu("", "", null);
        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems, rest2WithDayEnabledMenusAndItems);
    }

    @Test
    public void getFilteredByNameAndAddressWithDayEnabledMenu() {
        service.setTestDate(MenuTestData.TODAY);
        List<Restaurant> rests = service.getFilteredWithDayEnabledMenu("rest1", "address1", null);
        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems);
    }

    @Test
    public void getAllEnabledFilteredWithDayEnabledMenu() {
        service.setTestDate(MenuTestData.TODAY);
        List<Restaurant> rests = service.getFilteredWithDayEnabledMenu("", "", true);
        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems);
    }

    @Test
    public void update() {
        RestaurantTo updatedTo = asTo(getUpdated());
        service.update(updatedTo, REST1_ID);
        REST_MATCHER.assertMatch(service.getWithoutMenu(REST1_ID), getUpdated());
    }

    @Test
    public void enable() {
        service.enable(REST1_ID, false);
        assertFalse(service.getWithoutMenu(REST1_ID).isEnabled());
        service.enable(REST1_ID, true);
        assertTrue(service.getWithoutMenu(REST1_ID).isEnabled());
    }
}