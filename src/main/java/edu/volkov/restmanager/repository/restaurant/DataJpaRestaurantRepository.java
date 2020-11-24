package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.user.CrudUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestaurantRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaRestaurantRepository(
            CrudRestaurantRepository crudRestaurantRepository,
            CrudUserRepository crudUserRepository
    ) {
        this.crudRestaurantRepository = crudRestaurantRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return crudRestaurantRepository.save(restaurant);
    }

    @Override
    public boolean delete(Integer id) {
        return crudRestaurantRepository.delete(id) != 0;
    }

    @Override
    public Restaurant get(Integer id) {
        return crudRestaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getByName(String name) {
        return crudRestaurantRepository.getByName(name);
    }

    @Override
    public List<Restaurant> getAllWithoutMenu() {
        return crudRestaurantRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Restaurant> getAllWithDayMenu(LocalDate date) {
        return crudRestaurantRepository.getAllWithDayMenu(date, SORT_NAME);
    }

    public boolean decrementVoteQuantity(Integer restaurantId) {
        return crudRestaurantRepository.decrementVoteQuantity(restaurantId) != 0;
    }

    public boolean incrementVoteQuantity(Integer restaurantId) {
        return crudRestaurantRepository.incrementVoteQuantity(restaurantId) != 0;
    }
}
