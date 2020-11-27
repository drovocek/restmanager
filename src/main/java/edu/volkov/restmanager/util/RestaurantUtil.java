package edu.volkov.restmanager.util;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.RestaurantTo;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RestaurantUtil {
    public static RestaurantTo createToWithDifMenuType(Restaurant restaurant, boolean isEmptyMenu) {
        List<Menu> menus = (isEmptyMenu) ? Collections.emptyList() : restaurant.getMenus();
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getVotesQuantity(),
                menus
        );
    }

    public static RestaurantTo createToWithMenu(Restaurant restaurant) {
        return createToWithDifMenuType(restaurant, true);
    }


    public static List<RestaurantTo> getFilteredTosWithEmptyMenu(Collection<Restaurant> restaurants, Predicate<Restaurant> filter) {
        return restaurants.stream()
                .filter(filter)
                .map(restaurant -> createToWithDifMenuType(restaurant, true))
                .collect(Collectors.toList());
    }

    public static List<Restaurant> getFiltered(Collection<Restaurant> restaurants, Predicate<Restaurant> filter) {
        return restaurants.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static Predicate<Restaurant> getFilterByNameAndAddress(String name, String address) {
        String nameFilter = Optional.ofNullable(name).orElseGet(() -> "");
        String addressFilter = Optional.ofNullable(address).orElseGet(() -> "");
        return restaurant -> restaurant.getName().contains(nameFilter) &
                restaurant.getAddress().contains(addressFilter);
    }

    public static List<Restaurant> addMenuToRestaurantById(List<Menu> menus, List<Restaurant> restaurants) {
        Map<Integer, List<Menu>> menusById = menus.stream()
                .collect(Collectors.groupingBy(menu -> menu.getRestaurant().getId()));

        System.out.println(menusById);
        return restaurants.stream().peek(restaurant -> {
            restaurant.setMenus(
                    Optional.ofNullable(menusById.get(restaurant.getId())).orElseGet(ArrayList::new)
            );
        }).collect(Collectors.toList());
    }
}




