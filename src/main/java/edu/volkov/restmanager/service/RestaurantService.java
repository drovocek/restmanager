package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.restaurant.DataJpaRestaurantRepository;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public LocalTime changeTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    public RestaurantService(DataJpaRestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        return repository.save(restaurant);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(Integer id) {
        return checkNotFound(repository.get(id), "restaurant by id: " + id + "dos not exist");
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(repository.getByName(name), "name=" + name);
    }

    public List<Restaurant> getAllWithoutMenu() {
        return repository.getAllWithoutMenu();
    }

    public List<Restaurant> getAllWithDayMenu(LocalDate date) {
        return repository.getAllWithDayMenu(date);
    }

    //TODO bad???
    public void vote(int userId, int restaurantId, LocalDate voteDate) {
        boolean isVoteToDay = repository.hasUserVoteToDate(userId, voteDate);
        if (isVoteToDay & LocalTime.now().isBefore(changeTimeLimit)) {
            repository.deleteVote(userId, restaurantId, voteDate);
        } else if (!isVoteToDay) {
            repository.createVote(userId, restaurantId, voteDate);
        }
    }

    public void setChangeTimeLimit(LocalTime changeTimeLimit) {
        this.changeTimeLimit = changeTimeLimit;
    }
}
