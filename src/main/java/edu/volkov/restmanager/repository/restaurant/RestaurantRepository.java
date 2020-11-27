package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    //USER
    Restaurant getWithDayEnabledMenu(Integer id);

    List<Restaurant> getAllWithoutMenu();

    //ADMIN
    Restaurant save(Restaurant restaurant);

    boolean delete(Integer id);

    Restaurant get(Integer id);
}
