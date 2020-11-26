package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.RestaurantUtil.*;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;
    private final MenuService menuService;
    private final VoteService voteService;

    private LocalTime changeTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    public RestaurantService(RestaurantRepository repository, MenuService menuService, VoteService voteService) {
        this.repository = repository;
        this.menuService = menuService;
        this.voteService = voteService;
    }

    //USER
    @Transactional
    public List<Restaurant> getAllWithPreassignedQuantityEnabledMenu() {
        List<Menu> menus = menuService.getPreassignedQuantityEnabledMenu();
        List<Restaurant> restaurants = getEnabledWithoutMenu();
        return addMenusToRestaurantsById(menus, restaurants);
    }

    public List<Restaurant> getEnabledWithoutMenu() {
        return repository.getFilteredByEnabledWithoutMenu(true);
    }

    @Transactional
    public List<Restaurant> getFilteredByNameAndAddressWithEnabledMenu(String name, String address) {
        List<Menu> menus = menuService.getPreassignedQuantityEnabledMenu();
        List<Restaurant> filteredRestaurants = getFilteredByNameAndAddress(name, address);

        return addMenusToRestaurantsById(menus, filteredRestaurants);
    }

    public List<Restaurant> getFilteredByNameAndAddress(String name, String address) {
        Predicate<Restaurant> nameAndAddressFilter = getFilter(name, address);
        return getFiltered(repository.getFilteredByEnabledWithoutMenu(true), nameAndAddressFilter);
    }

    @Transactional
    public void vote(int userId, int restaurantId) {
        LocalDate votingDate = LocalDate.now();
        boolean beforeTimeLimit = LocalTime.now().isBefore(changeTimeLimit);
        Vote lastUserVoteToDate = voteService.get(userId, votingDate);

        if (lastUserVoteToDate == null) {
            voteService.constructAndCreate(userId, restaurantId, votingDate);
        } else if (lastUserVoteToDate.getRestaurant().getId() == restaurantId) {
            voteService.constructAndUpdate(lastUserVoteToDate.getId(), userId, restaurantId, votingDate);
        } else if (beforeTimeLimit) {
            voteService.delete(lastUserVoteToDate.getId());
        }
    }

    //ADMIN
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

    public void setChangeTimeLimit(LocalTime changeTimeLimit) {
        this.changeTimeLimit = changeTimeLimit;
    }

//    public List<Restaurant> getAllWithActiveMenuBetween(LocalDate startDate, LocalDate endDate) {
//        return restaurantRepository.getAllWithMenuBetween(atStartOfDayOrMin(startDate), atStartDayOrMax(endDate));
//    }
}
