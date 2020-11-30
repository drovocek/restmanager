package edu.volkov.restmanager.testdata;


import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.MenuItem;

import java.util.Arrays;
import java.util.List;

import static edu.volkov.restmanager.testdata.MenuTestData.*;

public class MenuItemTestData {
    public static final TestMatcher<MenuItem> MENU_ITEM_MATCHER = TestMatcher.usingIgnoringFieldsComparator(MenuItem.class, "menu");

    public final static Integer MENU_ITEM1_ID = 0;

    public static final MenuItem menuItem1 = new MenuItem(MENU_ITEM1_ID, "menItm1", menu1, 100);
    public static final MenuItem menuItem2 = new MenuItem(MENU_ITEM1_ID + 1, "menItm2", menu1, 101);
    public static final MenuItem menuItem3 = new MenuItem(MENU_ITEM1_ID + 2, "menItm3", menu1, 102);
    public static final MenuItem menuItem4 = new MenuItem(MENU_ITEM1_ID + 4, "menItm5", menu2, 104);
    public static final MenuItem menuItem5 = new MenuItem(MENU_ITEM1_ID + 5, "menItm6", menu2, 105);
    public static final MenuItem menuItem6 = new MenuItem(MENU_ITEM1_ID + 6, "menItm7", menu2, 106);
    public static final MenuItem menuItem7 = new MenuItem(MENU_ITEM1_ID + 7, "menItm8", menu3, 107);
    public static final MenuItem menuItem8 = new MenuItem(MENU_ITEM1_ID + 8, "menItm9", menu3, 108);
    public static final MenuItem menuItem9 = new MenuItem(MENU_ITEM1_ID + 9, "menItm10", menu3, 109);
    public static final MenuItem menuItem10 = new MenuItem(MENU_ITEM1_ID + 10, "menItm11", menu4, 1010);
    public static final MenuItem menuItem11 = new MenuItem(MENU_ITEM1_ID + 11, "menItm12", menu4, 1011);
    public static final MenuItem menuItem12 = new MenuItem(MENU_ITEM1_ID + 12, "menItm13", menu4, 1012);
    public static final MenuItem menuItem13 = new MenuItem(MENU_ITEM1_ID + 13, "menItm14", menu5, 1013);
    public static final MenuItem menuItem14 = new MenuItem(MENU_ITEM1_ID + 14, "menItm15", menu5, 1014);
    public static final MenuItem menuItem15 = new MenuItem(MENU_ITEM1_ID + 15, "menItm16", menu5, 1015);
    public static final MenuItem menuItem16 = new MenuItem(MENU_ITEM1_ID + 16, "menItm17", menu6, 1016);
    public static final MenuItem menuItem17 = new MenuItem(MENU_ITEM1_ID + 17, "menItm18", menu6, 1017);
    public static final MenuItem menuItem18 = new MenuItem(MENU_ITEM1_ID + 18, "menItm19", menu6, 1018);

    public static final List<MenuItem> allMenuItem = Arrays.asList(
            menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7, menuItem8,
            menuItem9, menuItem10, menuItem11, menuItem12, menuItem13, menuItem14, menuItem15, menuItem16
    );
    public static final List<MenuItem> menu1MenuItem = Arrays.asList(menuItem1, menuItem2, menuItem3);
    public static final List<MenuItem> menu2MenuItem = Arrays.asList(menuItem4, menuItem5, menuItem6);
    public static final List<MenuItem> menu3MenuItem = Arrays.asList(menuItem7, menuItem8, menuItem9);
    public static final List<MenuItem> menu4MenuItem = Arrays.asList(menuItem10, menuItem11, menuItem12);
    public static final List<MenuItem> menu5MenuItem = Arrays.asList(menuItem13, menuItem14, menuItem15);
    public static final List<MenuItem> menu6MenuItem = Arrays.asList(menuItem16, menuItem17, menuItem18);
}
