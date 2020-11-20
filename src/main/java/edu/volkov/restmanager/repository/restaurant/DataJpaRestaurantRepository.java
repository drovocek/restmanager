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

    @Override
    public Restaurant save(Restaurant restaurant) {
        return crudRepository.save(restaurant);
    }

    @Override
    public boolean delete(Integer id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Restaurant get(Integer id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getByName(String name) {
        return crudRepository.getByName(name);
    }

    @Override
    public List<Restaurant> getAllWithoutMenu() {
        return crudRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Restaurant> getAllWithDayMenu(LocalDate date) {
        return crudRepository.getAllWithDayMenu(date, SORT_NAME);
    }

    @Override
    public boolean deleteVote(Integer userId, Integer restaurantId, LocalDate voteDate) {
        return crudVoteRepository.delete(userId, restaurantId, voteDate) != 0;
    }

    @Override
    public void createLike(Integer userId, Integer restaurantId, LocalDate voteDate) {
        crudVoteRepository.createLike(userId, restaurantId, voteDate);
    }

    @Override
    public boolean hasUserVoteToDate(Integer userId, LocalDate voteDate) {
        return crudVoteRepository.hasUserVoteToDate(userId, voteDate) == 1;
    }
}
