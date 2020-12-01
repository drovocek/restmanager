package edu.volkov.restmanager.testdata;


import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.MenuItem;

import java.util.Arrays;
import java.util.List;

public class MenuItemTestData {
    public static final TestMatcher<MenuItem> MENU_ITEM_MATCHER = TestMatcher.usingIgnoringFieldsComparator(MenuItem.class, "menu", "enabled");

    public final static Integer MENU_ITEM1_ID = 0;
    public final static Integer MENU_ITEM_NOT_FOUND_ID = 20;

    public static final MenuItem menuItem1 = new MenuItem(MENU_ITEM1_ID, "menItm1", true, 100);
    public static final MenuItem menuItem2 = new MenuItem(MENU_ITEM1_ID + 1, "menItm2", true, 101);
    public static final MenuItem menuItem3 = new MenuItem(MENU_ITEM1_ID + 2, "menItm3", false, 102);
    public static final MenuItem menuItem4 = new MenuItem(MENU_ITEM1_ID + 3, "menItm4", true, 103);
    public static final MenuItem menuItem5 = new MenuItem(MENU_ITEM1_ID + 4, "menItm5", true, 104);
    public static final MenuItem menuItem6 = new MenuItem(MENU_ITEM1_ID + 5, "menItm6", false, 105);
    public static final MenuItem menuItem7 = new MenuItem(MENU_ITEM1_ID + 6, "menItm7", true, 106);
    public static final MenuItem menuItem8 = new MenuItem(MENU_ITEM1_ID + 7, "menItm8", true, 107);
    public static final MenuItem menuItem9 = new MenuItem(MENU_ITEM1_ID + 8, "menItm9", false, 108);
    public static final MenuItem menuItem10 = new MenuItem(MENU_ITEM1_ID + 9, "menItm10", true, 109);
    public static final MenuItem menuItem11 = new MenuItem(MENU_ITEM1_ID + 10, "menItm11", true, 1010);
    public static final MenuItem menuItem12 = new MenuItem(MENU_ITEM1_ID + 11, "menItm12", false, 1011);
    public static final MenuItem menuItem13 = new MenuItem(MENU_ITEM1_ID + 12, "menItm13", true, 1012);
    public static final MenuItem menuItem14 = new MenuItem(MENU_ITEM1_ID + 13, "menItm14", true, 1013);
    public static final MenuItem menuItem15 = new MenuItem(MENU_ITEM1_ID + 14, "menItm15", false, 1014);
    public static final MenuItem menuItem16 = new MenuItem(MENU_ITEM1_ID + 15, "menItm16", true, 1015);
    public static final MenuItem menuItem17 = new MenuItem(MENU_ITEM1_ID + 16, "menItm17", true, 1016);
    public static final MenuItem menuItem18 = new MenuItem(MENU_ITEM1_ID + 17, "menItm18", false, 1017);
    public static final MenuItem updatedMenuItem1 = new MenuItem(MENU_ITEM1_ID, "updatedMenuItem1", true, 7777);
    public static final MenuItem updatedMenuItem2 = new MenuItem(MENU_ITEM1_ID + 1, "updatedMenuItem2", false, 8888);


    public static final List<MenuItem> allMenuItems = Arrays.asList(
            menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7, menuItem8, menuItem9,
            menuItem10, menuItem11, menuItem12, menuItem13, menuItem14, menuItem15, menuItem16, menuItem17, menuItem18
    );

    public static final List<MenuItem> rest1AllMenuItems = Arrays.asList(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7, menuItem8, menuItem9);
    public static final List<MenuItem> rest1TodayMenuItems = Arrays.asList(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6);

    public static final List<MenuItem> menu1MenuItems = Arrays.asList(menuItem1, menuItem2, menuItem3);
    public static final List<MenuItem> menu2MenuItems = Arrays.asList(menuItem4, menuItem5, menuItem6);
    public static final List<MenuItem> menu3MenuItems = Arrays.asList(menuItem7, menuItem8, menuItem9);
    public static final List<MenuItem> menu4MenuItems = Arrays.asList(menuItem10, menuItem11, menuItem12);
    public static final List<MenuItem> menu5MenuItems = Arrays.asList(menuItem13, menuItem14, menuItem15);
    public static final List<MenuItem> menu6MenuItems = Arrays.asList(menuItem16, menuItem17, menuItem18);
    public static final List<MenuItem> updatedMenuItems = Arrays.asList(updatedMenuItem1, updatedMenuItem2);

    public static MenuItem getNew() {
        return new MenuItem(null, "newItmName", true, 111);
    }

    public static MenuItem getUpdated() {
        MenuItem updated = new MenuItem(menuItem1);
        updated.setName("updatedMenuItm");
        updated.setPrice(999);
        return updated;
    }
}
