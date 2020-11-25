package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import edu.volkov.restmanager.repository.vote.VoteRepository;
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

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;

    private LocalTime changeTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);
    private int dayMenuQuantity = 1;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            MenuRepository menuRepository, VoteRepository voteRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.voteRepository = voteRepository;
    }

    //USER
    @Transactional
    public List<Restaurant> getAllWithPreassignedQuantityEnabledMenu() {
        List<Menu> menus = getPreassignedQuantityEnabledMenu();
        List<Restaurant> restaurants = restaurantRepository.getFilteredByEnabledWithoutMenu(true);
        return addMenusToRestaurantsById(menus, restaurants);
    }

    @Transactional
    public List<Restaurant> getFilteredByNameAndAddressWithEnabledMenu(String name, String address) {
        Predicate<Restaurant> nameAndAddressFilter = getFilter(name, address);
        List<Menu> menus = getPreassignedQuantityEnabledMenu();
        List<Restaurant> filteredRestaurants = getFiltered(
                restaurantRepository.getFilteredByEnabledWithoutMenu(true), nameAndAddressFilter
        );

        return addMenusToRestaurantsById(menus, filteredRestaurants);
    }

    private List<Menu> getPreassignedQuantityEnabledMenu() {
        //TODO now()
//        LocalDate startDay = LocalDate.now();
        LocalDate startDate = LocalDate.of(2020, 1, 27);
        LocalDate endDate = startDate.plus(dayMenuQuantity - 1, ChronoUnit.DAYS);
        return menuRepository.getFilteredByEnabledBetweenDatesWithRestaurant(true, startDate, endDate);
    }

    @Transactional
    public void vote(int userId, int restaurantId, LocalDate voteDate) {
        boolean beforeTimeLimit = LocalTime.now().isBefore(changeTimeLimit);
        Vote lastUserVoteToDate = voteRepository.get(userId, voteDate);

        if (lastUserVoteToDate == null) {
            voteRepository.createAndSaveNewVote(userId, restaurantId, voteDate);
            restaurantRepository.incrementVoteQuantity(restaurantId);
        } else if (beforeTimeLimit && lastUserVoteToDate.getRestaurant().getId() == restaurantId) {
            voteRepository.delete(lastUserVoteToDate.getId());
            restaurantRepository.decrementVoteQuantity(restaurantId);
        }
    }


    //ADMIN
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

    public void setChangeTimeLimit(LocalTime changeTimeLimit) {
        this.changeTimeLimit = changeTimeLimit;
    }

//    public List<Restaurant> getAllWithActiveMenuBetween(LocalDate startDate, LocalDate endDate) {
//        return restaurantRepository.getAllWithMenuBetween(atStartOfDayOrMin(startDate), atStartDayOrMax(endDate));
//    }

    public void setDayMenuQuantity(int dayMenuQuantity) {
        if (dayMenuQuantity > 0) {
            this.dayMenuQuantity = dayMenuQuantity;
        }
    }


//    public List<Restaurant> getAllWithActiveMenu() {
//        //TODO now()
////        LocalDate startDay = LocalDate.now();
//        LocalDate startDay = LocalDate.of(2020, 1, 27);
//        LocalDate endDay = startDay.plus(dayMenuQuantity - 1, ChronoUnit.DAYS);
//
//        return restaurantRepository.getAllWithMenuBetween(startDay, endDay);
//    }
//
//    public List<Restaurant> getFilteredWithActiveMenu(String restaurantName, String restaurantAddress) {
//        //TODO now()
////        LocalDate startDay = LocalDate.now();
//        LocalDate startDay = LocalDate.of(2020, 1, 27);
//        LocalDate endDay = startDay.plus(dayMenuQuantity - 1, ChronoUnit.DAYS);
//
//        return restaurantRepository.getAllWithMenuBetween(startDay, endDay).stream()
//                .filter(getNameAddressFilter(restaurantName, restaurantAddress))
//                .collect(Collectors.toList());
//    }


}
