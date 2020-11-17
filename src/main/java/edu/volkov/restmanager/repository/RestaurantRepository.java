package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);

    boolean delete(Integer id);

    Restaurant get(Integer id);

    Restaurant getByName(String name);

    List<Restaurant> getAll();
}
