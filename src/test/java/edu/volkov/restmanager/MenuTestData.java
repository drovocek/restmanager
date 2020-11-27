package edu.volkov.restmanager;

import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static edu.volkov.restmanager.RestaurantTestData.*;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingIgnoringFieldsComparator("restaurant");

    public final static int MENU1_ID = 0;
    public final static int MENU_NOT_FOUND_ID = 15;
    public final static LocalDate today = LocalDate.of(2020, 1, 27);
    public final static LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);

    public static final Menu menu1 = new Menu(MENU1_ID, "menu1", rest1, today, false);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, "menu2", rest2, today, true);
    public static final Menu menu3 = new Menu(MENU1_ID + 2, "menu3", rest2, tomorrow, false);
    public static final Menu menu4 = new Menu(MENU1_ID + 3, "menu4", rest3, today, false);
    public static final Menu menu5 = new Menu(MENU1_ID + 4, "menu5", rest3, tomorrow, true);
    public static final Menu menu6 = new Menu(MENU1_ID + 5, "menu6", rest3, tomorrow, false);
    public static final Menu menu7 = new Menu(MENU1_ID + 6, "menu7", rest4, today, true);
    public static final Menu menu8 = new Menu(MENU1_ID + 7, "menu8", rest4, today, false);
    public static final Menu menu9 = new Menu(MENU1_ID + 8, "menu9", rest5, today, false);
    public static final Menu menu10 = new Menu(MENU1_ID + 9, "menu10", rest5, tomorrow, true);
    public static final Menu menu11 = new Menu(MENU1_ID + 10, "menu11", rest5, tomorrow, false);

    public static final List<Menu> allMenus = Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8, menu9, menu10, menu11);
    public static final List<Menu> todayMenus = Arrays.asList(menu1, menu2, menu4, menu7, menu8, menu9);
    public static final List<Menu> todayEnabledMenus = Arrays.asList(menu2, menu7);
    public static final List<Menu> allEnabledMenus = Arrays.asList(menu2, menu5, menu7, menu10);
}
