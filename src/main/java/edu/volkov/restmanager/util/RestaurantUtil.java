package edu.volkov.restmanager.util;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {
    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getVotesQuantity(),
                restaurant.getMenus()
        );
    }

    public static List<RestaurantTo> getToList(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).collect(Collectors.toList());
    }
}
