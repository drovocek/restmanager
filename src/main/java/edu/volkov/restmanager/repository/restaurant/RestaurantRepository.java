package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(Integer id);

    Restaurant get(Integer id);

    Restaurant getByName(String name);

    List<Restaurant> getAllWithoutMenu();

    List<Restaurant> getFilteredByEnabledWithoutMenu(boolean enabled);

    List<Restaurant> getAll();

    List<Restaurant> getAllByNameAndAddressPart(String name, String address);

    boolean incrementVoteQuantity(Integer restaurantId);

    boolean decrementVoteQuantity(Integer restaurantId);
}
