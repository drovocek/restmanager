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
import static org.assertj.core.api.Assertions.assertThat;

public class MenuTestData {

    public static TestMatcher<Menu> MENU_WITH_ITEMS_MATCHER = TestMatcher.usingAssertions(Menu.class,
            (a, e) -> assertThat(a).usingRecursiveComparison()
                    .ignoringFields("restaurant", "menuItems.menu").isEqualTo(e),
            (a, e) ->
                    assertThat(a).usingRecursiveComparison()
                            .ignoringFields("restaurant", "menuItems.menu").isEqualTo(e)
    );

    public static final int MENU1_ID = 0;
    public static final int MENU_NOT_FOUND_ID = 15;
    public static final LocalDate TODAY = LocalDate.of(2020, 1, 27);
    public static final LocalDate TOMORROW = TODAY.plus(1, ChronoUnit.DAYS);
    public static final LocalDate DAY_ABOUT = TOMORROW.plus(1, ChronoUnit.DAYS);

    public static final Menu menu1 = new Menu(MENU1_ID, "menu1", TODAY, true);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, "menu2", TOMORROW, false);
    public static final Menu menu3 = new Menu(MENU1_ID + 2, "menu3", DAY_ABOUT, true);
    public static final Menu menu4 = new Menu(MENU1_ID + 3, "menu4", TODAY, true);
    public static final Menu menu5 = new Menu(MENU1_ID + 4, "menu5", TOMORROW, false);
    public static final Menu menu6 = new Menu(MENU1_ID + 5, "menu6", DAY_ABOUT, true);

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

    public static final List<Menu> rest1AllMenusWithItems = Arrays.asList(menu3WithItems, menu2WithItems, menu1WithItems);
    public static final List<Menu> rest1MenusWithItems = orderByDateDesc(Arrays.asList(menu1WithItems, menu2WithItems, menu3WithItems));
    public static final List<Menu> rest2MenusWithItems = orderByDateDesc(Arrays.asList(menu4WithItems, menu5WithItems, menu6WithItems));
    public static final List<Menu> rest1DayEnabledMenusWithItems = orderByDateDesc(Arrays.asList(menu1WithItems));
    public static final List<Menu> rest2DayEnabledMenusWithItems = orderByDateDesc(Arrays.asList(menu4WithItems));
    public static final List<Menu> allMenusWithItems =
            Arrays.asList(menu6WithItems, menu5WithItems, menu4WithItems, menu3WithItems, menu1WithItems, menu2WithItems);
    public static final List<Menu> allMenus = orderByDateDesc(Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6));
    public static final List<Menu> allDayEnabledMenus = orderByDateDesc(Arrays.asList(menu2, menu4));
    public static final List<Menu> rest1Menus = orderByDateDesc(Arrays.asList(menu1, menu2, menu3));
    public static final List<Menu> rest1EnabledMenus = orderByDateDesc(Arrays.asList(menu2, menu3));
    public static final List<Menu> rest1DayEnabledMenus = orderByDateDesc(Arrays.asList(menu2));
    public static final List<Menu> rest2Menus = orderByDateDesc(Arrays.asList(menu4, menu5, menu6));
    public static final List<Menu> rest2EnabledMenus = orderByDateDesc(Arrays.asList(menu4, menu6));



    public static Menu getNew() {
        return new Menu(null, "newMenu", DAY_ABOUT.plus(1, ChronoUnit.DAYS), false);
    }

    public static Menu getNewWithMenuItems() {
        Menu newMenu = getNew();
        newMenu.setMenuItems(menu1MenuItems);
        return newMenu;
    }

    public static Menu getUpdated() {
        Menu updated = new Menu(menu1);
        updated.setName("updatedName");
        updated.setEnabled(false);
        return updated;
    }

    public static Menu getUpdatedWithMenuItems() {
        Menu updated = getUpdated();
        updated.setMenuItems(updatedMenuItems);
        return updated;
    }

    private static List<Menu> orderByDateDesc(List<Menu> menus) {
        Comparator<Menu> byId = Comparator.comparing(Menu::getMenuDate).reversed();
        return menus.stream().sorted(byId).collect(Collectors.toList());
    }
}
