package edu.volkov.restmanager.testdata;

import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.testdata.MenuItemTestData.*;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Menu.class, "restaurant", "menuItems");
    public static TestMatcher<Menu> MENU_WITH_ITEMS_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Menu.class, "restaurant");

    public static final int MENU1_ID = 0;
    public static final int MENU_NOT_FOUND_ID = 15;
    public static final LocalDate TODAY = LocalDate.of(2020, 1, 27);
    public static final LocalDate TOMORROW = TODAY.plus(1, ChronoUnit.DAYS);
    public static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    public static final Menu menu1 = new Menu(MENU1_ID, "menu1", TODAY, false);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, "menu2", TODAY, true);
    public static final Menu menu3 = new Menu(MENU1_ID + 2, "menu3", TOMORROW, true);
    public static final Menu menu4 = new Menu(MENU1_ID + 3, "menu4", TODAY, true);
    public static final Menu menu5 = new Menu(MENU1_ID + 4, "menu5", TOMORROW, false);
    public static final Menu menu6 = new Menu(MENU1_ID + 5, "menu6", TOMORROW, true);

    public static final Menu menu1WithItems = new Menu(menu1);
    public static final Menu menu2WithItems = new Menu(menu2);
    public static final Menu menu3WithItems = new Menu(menu3);
    public static final Menu menu4WithItems = new Menu(menu4);
    public static final Menu menu5WithItems = new Menu(menu5);
    public static final Menu menu6WithItems = new Menu(menu6);

    static {
        menu1WithItems.setMenuItems(menu1MenuItems);
        menu2WithItems.setMenuItems(menu2MenuItems);
        menu3WithItems.setMenuItems(menu3MenuItems);
        menu4WithItems.setMenuItems(menu4MenuItems);
        menu5WithItems.setMenuItems(menu5MenuItems);
        menu6WithItems.setMenuItems(menu6MenuItems);
    }

    public static final List<Menu> allMenus = orderByDateDesc(Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6));
    public static final List<Menu> allDayEnabledMenus = orderByDateDesc(Arrays.asList(menu2, menu4));

    public static final List<Menu> rest1Menus = orderByDateDesc(Arrays.asList(menu1, menu2, menu3));
    public static final List<Menu> rest1EnabledMenus = orderByDateDesc(Arrays.asList(menu2, menu3));
    public static final List<Menu> rest1DayEnabledMenus = orderByDateDesc(Arrays.asList(menu2));

    public static final List<Menu> rest2Menus = orderByDateDesc(Arrays.asList(menu4, menu5, menu6));
    public static final List<Menu> rest2EnabledMenus = orderByDateDesc(Arrays.asList(menu4, menu6));

    public static final List<Menu> rest1MenusWithItems = orderByDateDesc(Arrays.asList(menu1WithItems, menu2WithItems, menu3WithItems));
    public static final List<Menu> rest2MenusWithItems = orderByDateDesc(Arrays.asList(menu4WithItems, menu5WithItems, menu6WithItems));

    public static final List<Menu> rest1DayEnabledMenusWithItems = orderByDateDesc(Arrays.asList(menu2WithItems));
    public static final List<Menu> rest2DayEnabledMenusWithItems = orderByDateDesc(Arrays.asList(menu4WithItems));


    public static Menu getNew() {
        return new Menu(null, "newMenu", TOMORROW, false);
    }

    public static Menu getUpdated() {
        Menu updated = new Menu(menu1);
        updated.setMenuDate(TOMORROW);
        updated.setName("updatedName");
        updated.setEnabled(false);
        return updated;
    }

    private static List<Menu> orderByDateDesc(List<Menu> menus) {
        Comparator<Menu> byId = Comparator.comparing(Menu::getMenuDate).reversed();
        return menus.stream().sorted(byId).collect(Collectors.toList());
    }
}
