package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(Integer id);

    Restaurant get(Integer id);

    Restaurant getByName(String name);

    List<Restaurant> getAllWithoutMenu();

    List<Restaurant> getAllWithDayMenu(LocalDate date);

    boolean incrementVoteQuantity(Integer restaurantId);

    boolean decrementVoteQuantity(Integer restaurantId);
}
