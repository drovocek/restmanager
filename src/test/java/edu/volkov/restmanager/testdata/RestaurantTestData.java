package edu.volkov.restmanager.testdata;

import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.RestaurantTo;

import static edu.volkov.restmanager.testdata.MenuTestData.*;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> REST_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static TestMatcher<Restaurant> REST_MATCHER_WITH_MENU = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class);

    public static TestMatcher<RestaurantTo> REST_TO_MATCHER = TestMatcher.usingEqualsComparator(RestaurantTo.class);

    public final static int REST1_ID = 0;
    public final static int REST2_ID = REST1_ID + 1;
    public final static int REST_NOT_FOUND_ID = 10;

    public static final Restaurant rest1 = new Restaurant(REST1_ID, "rest1", "address1", "+7 (911) 111-1111", true, 4);
    public static final Restaurant rest2 = new Restaurant(REST2_ID, "rest2", "address2", "+7 (922) 222-2222", true, 4);
    public static final Restaurant duplicateNameRest = new Restaurant(null, "rest1", "address2", "+7 (966) 666-6666", true, 0);
    public static final Restaurant rest1WithAllMenusAndItems = new Restaurant(rest1);
    public static final Restaurant rest2WithAllMenusAndItems = new Restaurant(rest2);
    public static final Restaurant rest1WithDayEnabledMenusAndItems = new Restaurant(rest1);
    public static final Restaurant rest2WithDayEnabledMenusAndItems = new Restaurant(rest2);

    static {
        rest1WithAllMenusAndItems.setMenus(rest1MenusWithItems);
        rest2WithAllMenusAndItems.setMenus(rest2MenusWithItems);
        rest1WithDayEnabledMenusAndItems.setMenus(rest1DayEnabledMenusWithItems);
        rest2WithDayEnabledMenusAndItems.setMenus(rest2DayEnabledMenusWithItems);
    }
    public static Restaurant getNew() {
        return new Restaurant(null, "newRestaurant", "newAddress", "+7 (977) 777-7777", false, 0);
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(rest1);
        updated.setName("updatedRestaurant");
        updated.setAddress("updatedAddress");
        updated.setPhone("+7 (988) 888-8888");
        return updated;
    }
}
