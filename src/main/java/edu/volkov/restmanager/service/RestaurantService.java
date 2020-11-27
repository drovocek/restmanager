package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    //USER
    public Restaurant getWithDayEnabledMenu(Integer id) {
        return checkNotFoundWithId(repository.getWithDayEnabledMenu(id), id);
    }

    public List<Restaurant> getAllWithoutMenu() {
        return repository.getAllWithoutMenu();
    }

    //ADMIN
//    @Transactional
//    public List<Restaurant> getAllWithPreassignedQuantityEnabledMenu() {
//        List<Menu> menus = menuService.getPreassignedQuantityEnabledMenu();
//        List<Restaurant> restaurants = getEnabledWithoutMenu();
//        return addMenusToRestaurantsById(menus, restaurants);
//    }

//    public List<Restaurant> getEnabledWithoutMenu() {
//        return repository.getFilteredByEnabledWithoutMenu(true);
//    }
//
//    @Transactional
//    public List<Restaurant> getFilteredByNameAndAddressWithEnabledMenu(String name, String address) {
////        List<Menu> menus = menuService.getPreassignedQuantityEnabledMenu();
////        List<Restaurant> filteredRestaurants = getEnabledFilteredByNameAndAddress(name, address);
//
////        Predicate<Restaurant> nameAndAddressFilter = getFilter(name, address);
////        List<Restaurant> filteredRestaurants = getFiltered(getEnabled(), nameAndAddressFilter);
//
////        return addMenusToRestaurantsById(menus, filteredRestaurants);
//        List<Restaurant> filteredRestaurants = repository.getFilteredByEnabledWithoutMenu(true);
//        return filteredRestaurants;
//    }
//
//    public List<Restaurant> getEnabledFilteredByNameAndAddress(String name, String address) {
//        Predicate<Restaurant> nameAndAddressFilter = getFilter(name, address);
//        return getFiltered(repository.getFilteredByEnabledWithoutMenu(true), nameAndAddressFilter);
//    }
//
//    public List<Restaurant> getEnabled() {
//        return repository.getFilteredByEnabledWithoutMenu(true);
//    }

    //ADMIN
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        return repository.save(restaurant);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    public Restaurant get(Integer id) {
        return checkNotFound(repository.get(id), "restaurant by id: " + id + "dos not exist");
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    //    public Restaurant getByName(String name) {
//        Assert.notNull(name, "name must not be null");
//        return checkNotFound(repository.getByName(name), "name=" + name);
//    }
//
//    public List<Restaurant> getAllWithoutMenu() {
//        return repository.getAllWithoutMenu();
//    }
//
//    public void setChangeTimeLimit(LocalTime changeTimeLimit) {
//        this.changeTimeLimit = changeTimeLimit;
//    }
//
////    public List<Restaurant> getAllWithActiveMenuBetween(LocalDate startDate, LocalDate endDate) {
////        return restaurantRepository.getAllWithMenuBetween(atStartOfDayOrMin(startDate), atStartDayOrMax(endDate));
////    }
}
