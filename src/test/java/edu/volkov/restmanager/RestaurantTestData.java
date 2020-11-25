package edu.volkov.restmanager;

import edu.volkov.restmanager.model.Restaurant;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> REST_MATCHER = TestMatcher.usingIgnoringFieldsComparator("menus");

    public final static int REST1_ID = 0;
    public final static int REST_NOT_FOUND_ID = 10;

    public static final Restaurant rest1 = new Restaurant(REST1_ID, "rest1", "address1", "+7 (911) 111-1111",true,3);
    public static final Restaurant rest2 = new Restaurant(REST1_ID + 1, "rest2", "address2", "+7 (922) 222-2222",true,1);
    public static final Restaurant rest3 = new Restaurant(REST1_ID + 2, "rest3", "address3", "+7 (933) 333-3333",true,1);
    public static final Restaurant rest4 = new Restaurant(REST1_ID + 3, "rest4", "address4", "+7 (944) 444-4444",true,2);
    public static final Restaurant rest5 = new Restaurant(REST1_ID + 4, "rest5", "address5", "+7 (955) 555-5555",true,1);
    public static final Restaurant duplicateNameRest = new Restaurant(null, "rest1", "address2", "+7 (966) 666-6666");

    public static Restaurant getNew() {
        return new Restaurant(null, "newRestaurant", "newAddress", "+7 (977) 777-7777",false,0);
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(rest1);
        updated.setName("updatedRestaurant");
        updated.setAddress("updatedAddress");
        updated.setPhone("+7 (988) 888-8888");
        return updated;
    }
}
