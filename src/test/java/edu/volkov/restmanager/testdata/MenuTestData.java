package edu.volkov.restmanager.testdata;

import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static edu.volkov.restmanager.testdata.MenuItemTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.rest1;
import static edu.volkov.restmanager.testdata.RestaurantTestData.rest2;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Menu.class, "restaurant");

    public final static int MENU1_ID = 0;
    public final static int MENU_NOT_FOUND_ID = 15;
    public final static LocalDate TODAY = LocalDate.of(2020, 1, 27);
    public final static LocalDate TOMORROW = TODAY.plus(1, ChronoUnit.DAYS);

    public static final Menu menu1 = new Menu(MENU1_ID, "menu1", rest1, TODAY, false, menu1MenuItem);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, "menu2", rest1, TODAY, true, menu2MenuItem);
    public static final Menu menu3 = new Menu(MENU1_ID + 3, "menu3", rest1, TOMORROW, true, menu3MenuItem);
    public static final Menu menu4 = new Menu(MENU1_ID + 4, "menu4", rest2, TODAY, true, menu4MenuItem);
    public static final Menu menu5 = new Menu(MENU1_ID + 5, "menu5", rest2, TOMORROW, false, menu5MenuItem);
    public static final Menu menu6 = new Menu(MENU1_ID + 6, "menu6", rest2, TOMORROW, true, menu6MenuItem);

    public static final List<Menu> allMenus = Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6);
    public static final List<Menu> rest1Menus = Arrays.asList(menu1, menu2, menu3);
    public static final List<Menu> rest1EnabledMenus = Arrays.asList(menu2, menu3);
    public static final List<Menu> rest2Menus = Arrays.asList(menu4, menu5, menu6);
    public static final List<Menu> rest2EnabledMenus = Arrays.asList(menu4, menu6);


    public static Menu getNew() {
        return new Menu(null, "newMenu", rest1, TOMORROW, false);
    }

    public static Menu getUpdated() {
        Menu updated = new Menu(menu1);
        updated.setMenuDate(TOMORROW);
        updated.setName("updatedName");
        updated.setEnabled(false);
        return updated;
    }
}
