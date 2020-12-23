package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.RestaurantTo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static RestaurantTo asTo(Restaurant rest) {
        return new RestaurantTo(
                rest.id(),
                rest.getName(),
                rest.getAddress(),
                rest.getPhone(),
                rest.isEnabled()
        );
    }

    public static List<Restaurant> filtrate(List<Restaurant> rests, Predicate<Restaurant> filter) {
        return rests.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static List<Restaurant> addMenus(List<Restaurant> rests, List<Menu> menus) {
        Map<Integer, List<Menu>> menusByRestId = menus.stream()
                .filter(Menu::isEnabled)
                .collect(Collectors.groupingBy(menu -> menu.getRestaurant().getId()));

        rests.forEach(rest ->
                rest.setMenus(Optional.ofNullable(menusByRestId.get(rest.getId())).orElse(Collections.emptyList()))
        );

        return rests;
    }

    public static Predicate<Restaurant> getFilterByNameAndAddress(String name, String address) {
        String nameFilter = Optional.ofNullable(name).orElse("");
        String addressFilter = Optional.ofNullable(address).orElse("");

        return restaurant -> restaurant.getName().contains(nameFilter) &
                restaurant.getAddress().contains(addressFilter);
    }

    public static Restaurant updateFromTo(Restaurant restaurant, RestaurantTo restaurantTo) {
        restaurant.setName(restaurantTo.getName());
        restaurant.setAddress(restaurantTo.getAddress());
        restaurant.setPhone(restaurantTo.getPhone());
        return restaurant;
    }
}




