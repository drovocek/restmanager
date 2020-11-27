package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.MenuTestData.*;
import static edu.volkov.restmanager.RestaurantTestData.REST1_ID;
import static org.junit.Assert.assertThrows;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    protected MenuService service;

    @Test
    public void create() {
        Menu created = service.create(getNew(), REST1_ID);
        int newId = created.id();
        Menu newMenu = getNew();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(service.get(newId), newMenu);
    }

    @Test
    public void delete() {
        service.delete(MENU1_ID, REST1_ID);
        assertThrows(NotFoundException.class, () -> service.get(MENU1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MENU1_ID, REST1_ID + 1));
    }

    @Test
    public void get() {
        Menu menu = service.get(MENU1_ID);
        MENU_MATCHER.assertMatch(menu, menu1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MENU_NOT_FOUND_ID));
    }

    @Test
    public void update() {
        Menu updated = getUpdated();
        service.update(updated, updated.getRestaurant().getId());
        MENU_MATCHER.assertMatch(service.get(MENU1_ID), getUpdated());
    }

    @Test
    public void getAll() {
        Comparator<Menu> byDate = Comparator.comparing(Menu::getMenuDate).reversed();
        List<Menu> all = service.getAll();
        MENU_MATCHER.assertMatch(all, allMenus.stream().sorted(byDate).collect(Collectors.toList()));
    }

    @Test
    public void getByRestIdBetweenDatesOpenBoarders() {
        LocalDate startDate = LocalDate.of(2010,1,1);
        LocalDate endDate = LocalDate.of(2030,1,1);
        List<Menu> betweenDates = service.getByRestIdBetweenDates(REST1_ID + 4, startDate, endDate);
        MENU_MATCHER.assertMatch(betweenDates, menu10, menu11, menu9);
    }

    @Test
    public void getByRestIdBetweenDatesAll() {
        LocalDate startDate = TODAY;
        LocalDate endDate = TOMORROW;
        List<Menu> betweenDates = service.getByRestIdBetweenDates(REST1_ID + 4, startDate, endDate);
        MENU_MATCHER.assertMatch(betweenDates, menu10, menu11, menu9);
    }

    @Test
    public void getByRestIdBetweenOneDay() {
        LocalDate startDate = TODAY;
        LocalDate endDate = TODAY;
        List<Menu> betweenDates = service.getByRestIdBetweenDates(REST1_ID + 4, startDate, endDate);
        MENU_MATCHER.assertMatch(betweenDates, menu9);
    }
}
