package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.AbstractTest;
import edu.volkov.restmanager.model.AbstractBaseEntity;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.testdata.MenuItemTestData.*;
import static edu.volkov.restmanager.testdata.MenuTestData.getNew;
import static edu.volkov.restmanager.testdata.MenuTestData.getUpdated;
import static edu.volkov.restmanager.testdata.MenuTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MenuRepositoryTest extends AbstractTest {

    @Autowired
    protected MenuRepository repository;

    @Test
    public void create() {
        Menu created = repository.save(getNew(), REST1_ID);
        int newId = created.id();
        Menu newMenu = getNew();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(repository.get(newId, REST1_ID), newMenu);
    }

    @Test
    public void delete() {
        repository.delete(MENU1_ID, REST1_ID);
        assertNull(repository.get(MENU1_ID, REST1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertFalse(repository.delete(MENU1_ID, REST1_ID + 1));
    }

    @Test
    public void get() {
        Menu menu = repository.get(MENU1_ID, REST1_ID);
        MENU_MATCHER.assertMatch(menu, menu1);
    }

    @Test
    public void getNotFound() {
        assertNull(repository.get(MENU_NOT_FOUND_ID, REST1_ID));
    }

    @Test
    public void update() {
        Menu updated = getUpdated();
        repository.save(updated, REST1_ID);
        Menu actual = repository.get(MENU1_ID, REST1_ID);
        MENU_MATCHER.assertMatch(actual, getUpdated());
    }

    @Test
    public void updateNotFound() {
        Menu updated = getUpdated();
        assertNull(repository.save(updated, REST1_ID + 1));
    }

    @Test
    public void getAll() {
        List<Menu> all = repository.getAll(REST1_ID);
        MENU_MATCHER.assertMatch(all, rest1Menus);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(all),
                rest1AllMenuItems
        );
    }

    @Test
    public void getByRestIdBetweenDatesOpenBoarders() {
        List<Menu> betweenDates = repository.getBetween(MIN_DATE, MAX_DATE, REST1_ID);
        MENU_MATCHER.assertMatch(betweenDates, rest1Menus);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(betweenDates),
                rest1AllMenuItems
        );
    }

    @Test
    public void getByRestIdBetweenDatesAll() {
        List<Menu> betweenDates = repository.getBetween(TODAY, TOMORROW, REST1_ID);
        MENU_MATCHER.assertMatch(betweenDates, rest1Menus);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(betweenDates),
                rest1AllMenuItems
        );
    }

    @Test
    public void getByRestIdBetweenOneDay() {
        List<Menu> betweenDates = repository.getBetween(TODAY, TODAY, REST1_ID);
        MENU_MATCHER.assertMatch(betweenDates, menu1, menu2);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(betweenDates),
                rest1TodayMenuItems
        );
    }

    private List<MenuItem> extractMenuItemsOrderById(List<Menu> menus) {
        Comparator<MenuItem> byId = Comparator.comparing(AbstractBaseEntity::getId);
        return menus.stream()
                .flatMap(menu -> menu.getMenuItems().stream())
                .sorted(byId)
                .collect(Collectors.toList());
    }
}
