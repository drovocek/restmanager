package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static edu.volkov.restmanager.testdata.MenuTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
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
        MENU_MATCHER.assertMatch(service.get(newId,REST1_ID), newMenu);
    }
//
//    @Test
//    public void delete() {
//        service.delete(MENU1_ID, REST1_ID);
//        assertThrows(NotFoundException.class, () -> service.get(MENU1_ID,REST1_ID));
//    }
//
//    @Test
//    public void deletedNotFound() {
//        assertThrows(NotFoundException.class, () -> service.delete(MENU1_ID, REST1_ID + 1));
//    }
//
//    @Test
//    public void get() {
//        Menu menu = service.get(MENU1_ID, REST1_ID);
//        MENU_MATCHER.assertMatch(menu, menu1);
//        MENU_ITEM_MATCHER.assertMatch(menu.getMenuItems(),menu1MenuItem);
//    }
//
//    @Test
//    public void getNotFound() {
//        assertThrows(NotFoundException.class, () -> service.get(MENU_NOT_FOUND_ID,REST1_ID));
//    }
//
//    @Test
//    public void update() {
//        Menu updated = getUpdated();
//        service.update(updated, updated.getRestaurant().getId());
//        MENU_MATCHER.assertMatch(service.get(MENU1_ID,REST1_ID), getUpdated());
//        MENU_ITEM_MATCHER.assertMatch(service.get(MENU1_ID,REST1_ID).getMenuItems(),menu1MenuItem);
//    }
//
//    @Test
//    public void getAll() {
//        Comparator<Menu> byDate = Comparator.comparing(Menu::getMenuDate).reversed();
//        Comparator<MenuItem> byId = Comparator.comparing(MenuItem::getId);
//        List<Menu> all = service.getAll();
//        MENU_MATCHER.assertMatch(all, allMenus.stream().sorted(byDate).collect(Collectors.toList()));
//        List<MenuItem> allMenuItems = all.stream().flatMap(menu->menu.getMenuItems().stream()).sorted(byId).collect(Collectors.toList());
//        MENU_ITEM_MATCHER.assertMatch(allMenuItems, allMenuItem);
//    }
//
//    @Test
//    public void getByRestIdBetweenDatesOpenBoarders() {
//        Comparator<MenuItem> byId = Comparator.comparing(MenuItem::getId);
//        LocalDate startDate = LocalDate.of(2010, 1, 1);
//        LocalDate endDate = LocalDate.of(2030, 1, 1);
//        List<Menu> betweenDates = service.getByRestIdBetweenDates(REST1_ID + 4, startDate, endDate);
//        List<MenuItem> allMenuItems = betweenDates.stream().flatMap(menu->menu.getMenuItems().stream()).sorted(byId).collect(Collectors.toList());
//        MENU_MATCHER.assertMatch(betweenDates, menu10, menu11, menu9);
//        MENU_ITEM_MATCHER.assertMatch(allMenuItems);
//    }
//
//    @Test
//    public void getByRestIdBetweenDatesAll() {
//        LocalDate startDate = TODAY;
//        LocalDate endDate = TOMORROW;
//        List<Menu> betweenDates = service.getByRestIdBetweenDates(REST1_ID + 4, startDate, endDate);
//        MENU_MATCHER.assertMatch(betweenDates, menu10, menu11, menu9);
//    }
//
//    @Test
//    public void getByRestIdBetweenOneDay() {
//        LocalDate startDate = TODAY;
//        LocalDate endDate = TODAY;
//        List<Menu> betweenDates = service.getByRestIdBetweenDates(REST1_ID + 4, startDate, endDate);
//        MENU_MATCHER.assertMatch(betweenDates, menu9);
//    }
}
