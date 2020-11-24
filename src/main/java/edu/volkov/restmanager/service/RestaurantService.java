package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import edu.volkov.restmanager.repository.user.UserRepository;
import edu.volkov.restmanager.repository.vote.VoteRepository;
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

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public LocalTime changeTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            UserRepository userRepository,
            VoteRepository voteRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }


    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        return restaurantRepository.save(restaurant);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }

    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id), id);
    }

    public Restaurant get(Integer id) {
        return checkNotFound(restaurantRepository.get(id), "restaurant by id: " + id + "dos not exist");
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(restaurantRepository.getByName(name), "name=" + name);
    }

    public List<Restaurant> getAllWithoutMenu() {
        return restaurantRepository.getAllWithoutMenu();
    }

    public List<Restaurant> getAllWithDayMenu(LocalDate date) {
        return restaurantRepository.getAllWithDayMenu(date);
    }

    public void vote(int userId, int restaurantId, LocalDate voteDate) {
        boolean beforeTimeLimit = LocalTime.now().isBefore(changeTimeLimit);
        Vote lastUserVoteToDate = voteRepository.get(userId, voteDate);

        if (lastUserVoteToDate == null) {
            voteRepository.createAndSaveNewVote(userId, restaurantId, voteDate);
            restaurantRepository.incrementVoteQuantity(restaurantId);
        } else if (beforeTimeLimit) {
            voteRepository.delete(userId, voteDate);
            restaurantRepository.decrementVoteQuantity(restaurantId);
        }
    }

    public void setChangeTimeLimit(LocalTime changeTimeLimit) {
        this.changeTimeLimit = changeTimeLimit;
    }
}
