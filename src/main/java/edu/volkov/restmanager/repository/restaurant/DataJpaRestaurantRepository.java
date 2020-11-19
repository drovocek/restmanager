package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.like.CrudVoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRepository;
    private final CrudVoteRepository crudVoteRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRepository, CrudVoteRepository crudVoteRepository) {
        this.crudRepository = crudRepository;
        this.crudVoteRepository = crudVoteRepository;
    }

    public Restaurant save(Restaurant restaurant) {
        return crudRepository.save(restaurant);
    }

    public boolean delete(Integer id) {
        return crudRepository.delete(id) != 0;
    }

    public Restaurant get(Integer id) {
        return crudRepository.findById(id).orElse(null);
    }

    public Restaurant getByName(String name) {
        return crudRepository.getByName(name);
    }

    public List<Restaurant> getAll() {
        return crudRepository.findAll(SORT_NAME);
    }

    public boolean deleteVote(Integer userId, Integer restaurantId, LocalDate voteDate) {
        return crudVoteRepository.delete(userId, restaurantId, voteDate) != 0;
    }

    public void createLike(Integer userId, Integer restaurantId, LocalDate voteDate) {
        crudVoteRepository.createLike(userId, restaurantId, voteDate);
    }

    public boolean hasUserVoteToDate(Integer userId, LocalDate voteDate) {
        return crudVoteRepository.hasUserVoteToDate(userId, voteDate) == 1;
    }
}
