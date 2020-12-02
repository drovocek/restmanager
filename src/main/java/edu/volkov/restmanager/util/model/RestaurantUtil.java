package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.RestaurantTo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RestaurantUtil {
    public static RestaurantTo createToWithMenu(Restaurant rest) {
        return new RestaurantTo(
                rest.id(),
                rest.getName(),
                rest.getAddress(),
                rest.getPhone(),
                rest.getVotesQuantity(),
                rest.getMenus()
        );
    }

    public static RestaurantTo createToWithoutMenu(Restaurant rest) {
        return new RestaurantTo(
                rest.id(),
                rest.getName(),
                rest.getAddress(),
                rest.getPhone(),
                rest.getVotesQuantity(),
                Collections.emptyList()
        );
    }


    public static List<RestaurantTo> getFilteredTosWithMenu(List<Restaurant> rests, Predicate<Restaurant> filter) {
        return rests.stream()
                .filter(filter)
                .map(RestaurantUtil::createToWithMenu)
                .collect(Collectors.toList());
    }

    public static Predicate<Restaurant> getFilterByNameAndAddress(String name, String address) {
        String nameFilter = Optional.ofNullable(name).orElse("");
        String addressFilter = Optional.ofNullable(address).orElse("");

        return restaurant -> restaurant.getName().contains(nameFilter) &
                restaurant.getAddress().contains(addressFilter);
    }
}




