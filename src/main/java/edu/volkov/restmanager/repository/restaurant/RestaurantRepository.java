package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(Integer id);

    Restaurant get(Integer id);

    Restaurant getByName(String name);

    List<Restaurant> getAll();

    boolean deleteVote(Integer userId, Integer restaurantId, LocalDate voteDate);

    void createLike(Integer userId, Integer restaurantId, LocalDate voteDate);

    boolean hasUserVoteToDate(Integer userId, LocalDate voteDate);
}
