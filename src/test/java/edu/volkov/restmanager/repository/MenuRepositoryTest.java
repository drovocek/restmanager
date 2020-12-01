package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.model.AbstractBaseEntity;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import edu.volkov.restmanager.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.testdata.MenuItemTestData.MENU_ITEM_MATCHER;
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

    //TODO NEED FIX
    @Test
    public void update() {
        Menu updated = getUpdated();
        repository.save(updated, REST1_ID);
        System.out.println("!!!!!!!!!");
        Menu actual = repository.get(MENU1_ID, REST1_ID);
        MENU_MATCHER.assertMatch(actual, getUpdated());
    }

    @Test
    public void updateNotFound() {
        Menu updated = getUpdated();
        assertNull(repository.save(updated, REST1_ID + 1));
    }

    @Test
    public void getByRestIdBetweenDatesOpenBoarders() {
        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);

        List<Menu> betweenDates = repository.getBetween(startDate, endDate, REST1_ID);
        MENU_MATCHER.assertMatch(betweenDates, rest1Menus);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(betweenDates),
                extractMenuItemsOrderById(rest1Menus)
        );
    }

    @Test
    public void getByRestIdBetweenDatesAll() {
        List<Menu> betweenDates = repository.getBetween(TODAY, TOMORROW, REST1_ID);
        MENU_MATCHER.assertMatch(betweenDates, rest1Menus);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(betweenDates),
                extractMenuItemsOrderById(rest1Menus)
        );
    }

    @Test
    public void getByRestIdBetweenOneDay() {
        List<Menu> betweenDates = repository.getBetween(TODAY, TODAY, REST1_ID);
        MENU_MATCHER.assertMatch(betweenDates, menu1, menu2);
        MENU_ITEM_MATCHER.assertMatch(
                extractMenuItemsOrderById(betweenDates),
                extractMenuItemsOrderById(Arrays.asList(menu1, menu2))
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
