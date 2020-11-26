package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    //USER
    List<Restaurant> getAll();

    //ADMIN
    Restaurant save(Restaurant restaurant);

    boolean delete(Integer id);

    Restaurant get(Integer id);

    Restaurant getByName(String name);

    List<Restaurant> getAllWithoutMenu();

    List<Restaurant> getBetweenWithEnabledMenu(LocalDate startDate, LocalDate endDate);
}
