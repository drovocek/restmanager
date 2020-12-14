package edu.volkov.restmanager.service;

import edu.volkov.restmanager.AbstractTest;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.testdata.MenuItemTestData;
import edu.volkov.restmanager.to.MenuItemTo;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.exception.NotFoundException;
import edu.volkov.restmanager.util.model.MenuItemUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static edu.volkov.restmanager.testdata.MenuTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.util.model.MenuUtil.asTo;
import static org.junit.Assert.*;

public class MenuServiceTest extends AbstractTest {

    @Autowired
    protected MenuService service;

    @Test
    public void createWithMenuItems() {
        Menu createdMenu = service.createWithMenuItems(REST1_ID, getNewWithMenuItems());
        int newId = createdMenu.id();
        Menu newMenu = getNewWithMenuItems();
        newMenu.setId(newId);
        MENU_WITH_ITEMS_MATCHER.assertMatch(createdMenu, newMenu);
        MENU_WITH_ITEMS_MATCHER.assertMatch(service.getWithMenuItems(REST1_ID, newId), newMenu);
    }

    @Test
    public void delete() {
        service.delete(REST1_ID, MENU1_ID);
        assertThrows(NotFoundException.class, () -> service.getWithMenuItems(REST1_ID, MENU1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(REST1_ID, MENU_NOT_FOUND_ID));
    }

//    @Test
//    public void getWithoutMenu() {
//        Restaurant rest = service.getWithoutMenu(REST1_ID);
//        REST_MATCHER.assertMatch(rest, rest1);
//    }

//    @Test
//    public void getWithoutMenuNotFound() {
//        assertThrows(NotFoundException.class, () -> service.getWithoutMenu(REST_NOT_FOUND_ID));
//    }
//
//    @Test
//    public void getWithEnabledMenu() {
//        service.setTestDate(MenuTestData.TODAY);
//        Restaurant rest = service.getWithEnabledMenu(REST1_ID);
//        REST_WITH_MENU_MATCHER.assertMatch(rest, rest1WithDayEnabledMenusAndItems);
//    }
//
//    @Test
//    public void getAllWithDayEnabledMenu() {
//        service.setTestDate(MenuTestData.TODAY);
//        List<Restaurant> rests = service.getAllWithDayEnabledMenu();
//        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems, rest2WithDayEnabledMenusAndItems);
//    }
//
//    @Test
//    public void getAllEnabledWithDayEnabledMenu() {
//        service.setTestDate(MenuTestData.TODAY);
//        List<Restaurant> rests = service.getAllEnabledWithDayEnabledMenu();
//        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems);
//    }
//
//    @Test
//    public void getAllFilteredWithDayEnabledMenu() {
//        service.setTestDate(MenuTestData.TODAY);
//        List<Restaurant> rests = service.getFilteredWithDayEnabledMenu("", "", null);
//        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems, rest2WithDayEnabledMenusAndItems);
//    }
//
//    @Test
//    public void getFilteredByNameAndAddressWithDayEnabledMenu() {
//        service.setTestDate(MenuTestData.TODAY);
//        List<Restaurant> rests = service.getFilteredWithDayEnabledMenu("rest1", "address1", null);
//        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems);
//    }
//
//    @Test
//    public void getAllEnabledFilteredWithDayEnabledMenu() {
//        service.setTestDate(MenuTestData.TODAY);
//        List<Restaurant> rests = service.getFilteredWithDayEnabledMenu("", "", true);
//        REST_WITH_MENU_MATCHER.assertMatch(rests, rest1WithDayEnabledMenusAndItems);
//    }

    @Test
    public void updateWithMenuItems() {
        MenuTo updatedMenuTo = asTo(getUpdatedWithMenuItems());
        service.updateWithMenuItems(REST1_ID, updatedMenuTo);
        MENU_WITH_ITEMS_MATCHER.assertMatch(service.getWithMenuItems(REST1_ID, MENU1_ID), getUpdatedWithMenuItems());
    }

    @Test
    public void enable() {
        service.enable(REST1_ID, MENU1_ID, false);
        assertFalse(service.getWithMenuItems(REST1_ID, MENU1_ID).isEnabled());
        service.enable(REST1_ID, MENU1_ID, true);
        assertTrue(service.getWithMenuItems(REST1_ID, MENU1_ID).isEnabled());
    }

//    @Test
//    public void createWithException() {
//        validateRootCause(() -> service.create(new Restaurant(" ", "address1", "+7 (911) 111-1111")), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new Restaurant("rest1", " ", "+7 (911) 111-1111")), ConstraintViolationException.class);
//        validateRootCause(() -> service.create(new Restaurant("rest1", "address1", "+10 (911) 111-1111")), ConstraintViolationException.class);
//    }
}