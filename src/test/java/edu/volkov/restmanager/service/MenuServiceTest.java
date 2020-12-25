package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
    public void updateWithMenuItems() {
        MenuTo updatedMenuTo = asTo(getUpdatedWithMenuItems());
        service.updateWithMenuItems(REST1_ID, MENU1_ID, updatedMenuTo);
        MENU_WITH_ITEMS_MATCHER.assertMatch(service.getWithMenuItems(REST1_ID, MENU1_ID), getUpdatedWithMenuItems());
    }

    @Test
    public void updateEmptyMenuItems() {
        Menu updated = getUpdatedWithMenuItems();
        updated.setMenuItems(Collections.emptyList());
        MenuTo updatedTo = asTo(updated);
        service.updateWithMenuItems(REST1_ID, MENU1_ID, updatedTo);

        Menu updatedTest = getUpdatedWithMenuItems();
        updatedTest.setMenuItems(Collections.emptyList());
        MENU_WITH_ITEMS_MATCHER.assertMatch(service.getWithMenuItems(REST1_ID, MENU1_ID), updatedTest);
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

    @Test
    public void getWithMenuItems() {
        Menu menu = service.getWithMenuItems(REST1_ID, MENU1_ID);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menu, menu1WithItems);
    }

    @Test
    public void getWithMenuItemsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMenuItems(REST1_ID, MENU_NOT_FOUND_ID));
    }

    @Test
    public void getAllForRestWithMenuItems() {
        List<Menu> menus = service.getAllForRestWithMenuItems(REST1_ID);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menus, rest1MenusWithItems);
    }

    @Test
    public void getFilteredByNullForRestWithMenuItems() {
        List<Menu> menus = service.getFilteredForRestWithMenuItems(REST1_ID, null, null, null);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menus, rest1AllMenusWithItems);
    }

    @Test
    public void getFilteredByDateForRestWithMenuItems() {
        List<Menu> menus = service.getFilteredForRestWithMenuItems(REST1_ID, TODAY, TODAY, null);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menus, menu1WithItems, menu2WithItems);
    }

    @Test
    public void getFilteredByEnabledForRestWithMenuItems() {
        List<Menu> menus = service.getFilteredForRestWithMenuItems(REST1_ID, null, null, true);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menus, menu3WithItems, menu2WithItems);
    }

    @Test
    public void enable() {
        service.enable(REST1_ID, MENU1_ID, false);
        assertFalse(service.getWithMenuItems(REST1_ID, MENU1_ID).isEnabled());

        service.enable(REST1_ID, MENU1_ID, true);
        assertTrue(service.getWithMenuItems(REST1_ID, MENU1_ID).isEnabled());
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> service.createWithMenuItems(REST1_ID, new Menu(null, "", LocalDate.now(), true)), ConstraintViolationException.class);
        validateRootCause(() -> service.createWithMenuItems(REST1_ID, new Menu(null, " ", LocalDate.now(), true)), ConstraintViolationException.class);
    }
}