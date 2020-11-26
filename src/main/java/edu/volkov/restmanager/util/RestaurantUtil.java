package edu.volkov.restmanager.util;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.RestaurantTo;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RestaurantUtil {
    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getVotes().size(),
                restaurant.getMenus().stream().collect(Collectors.toList())
        );
    }

    public static List<RestaurantTo> getTo(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).collect(Collectors.toList());
    }

    public static List<RestaurantTo> getFilteredTo(List<Restaurant> restaurants, Predicate<Restaurant> restaurantFilter) {
        return restaurants.stream()
                .filter(restaurantFilter)
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    public static List<RestaurantTo> getToWithActiveMenu(List<Restaurant> restaurants) {
        return getFilteredToWithFilteredMenu(restaurants, restaurant -> true, menu -> menu.isEnabled());
    }

    public static List<RestaurantTo> getFilteredToWithActiveMenu(List<Restaurant> restaurants, Predicate<Restaurant> restaurantFilter) {
        return getFilteredToWithFilteredMenu(restaurants, restaurantFilter, menu -> menu.isEnabled());
    }

    public static List<RestaurantTo> getFilteredToWithFilteredMenu(
            List<Restaurant> restaurants,
            Predicate<Restaurant> restaurantFilter,
            Predicate<Menu> menuFilter
    ) {
        return restaurants.stream()
                .filter(restaurantFilter)
                .map(restaurant -> getRestaurantWithFilteredMenu(restaurant, menuFilter))
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    private static Restaurant getRestaurantWithFilteredMenu(Restaurant restaurant, Predicate<Menu> menuFilter) {
        List<Menu> menus = restaurant.getMenus().stream().filter(menuFilter).collect(Collectors.toList());
        restaurant.setMenus(menus);
        return restaurant;
    }

    private static List<Menu> getFilteredMenu(Collection<Menu> menus, Predicate<Menu> filter) {
        return menus.stream().filter(filter).collect(Collectors.toList());
    }

    public static Predicate<Restaurant> getNameAddressFilter(String restaurantName, String restaurantAddress) {
        String name = Optional.ofNullable(restaurantName).orElse("");
        String address = Optional.ofNullable(restaurantAddress).orElse("");

        return restaurant -> restaurant.getName().contains(name) &
                restaurant.getAddress().contains(address);
    }

    public static List<Restaurant> addMenusToRestaurantsById(List<Menu> menus, List<Restaurant> restaurants) {
        Map<Integer, List<Menu>> menusById = menus.stream()
                .collect(Collectors.groupingBy(menu -> menu.getRestaurant().getId()));

        System.out.println(menusById);
        return restaurants.stream().peek(restaurant -> {
            restaurant.setMenus(
                    Optional.ofNullable(menusById.get(restaurant.getId())).orElseGet(ArrayList::new)
            );
        }).collect(Collectors.toList());
    }

    public static List<Restaurant> getFiltered(Collection<Restaurant> restaurants, Predicate<Restaurant> filter) {
        return restaurants.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static Predicate<Restaurant> getFilter(String name, String address) {
        String nameFilter = Optional.ofNullable(name).orElseGet(() -> "");
        String addressFilter = Optional.ofNullable(address).orElseGet(() -> "");
        return restaurant -> restaurant.getName().contains(nameFilter) &
                restaurant.getAddress().contains(addressFilter);
    }
}




