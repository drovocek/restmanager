package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.CrudMenuRepository;
import edu.volkov.restmanager.repository.CrudRestaurantRepository;
import edu.volkov.restmanager.to.RestaurantTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;
import static edu.volkov.restmanager.util.model.RestaurantUtil.addMenus;
import static edu.volkov.restmanager.util.model.RestaurantUtil.updateFromTo;

@RequiredArgsConstructor
@Service
@Slf4j
public class RestaurantService {

    private final Sort sortByName = Sort.by(Sort.Direction.ASC, "name");
    private LocalDate testDate;

    private final CrudRestaurantRepository restRepo;
    private final CrudMenuRepository menuRepo;

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant");
        Assert.notNull(restaurant, "restaurant must not be null");
        return restRepo.save(restaurant);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(RestaurantTo restTo, int id) {
        log.info("update restaurant: {}", restTo.id());
        Assert.notNull(restTo, "restTo must not be null");
        updateFromTo(getWithoutMenu(id), restTo);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        log.info("delete restaurant: {}", id);
        checkNotFoundWithId(restRepo.delete(id) != 0, id);
    }

    public Restaurant getWithoutMenu(int id) {
        log.info("getWithoutMenu restaurant: {}", id);
        return checkNotFound(restRepo.findById(id).orElse(null), "restaurant by id: " + id + "dos not exist");
    }

    @Transactional
    public Restaurant getWithEnabledMenu(int id) {
        log.info("getWithEnabledMenu restaurant: {}", id);
        LocalDate toDay = (testDate == null) ? LocalDate.now() : testDate;
        Restaurant rest = getWithoutMenu(id);
        List<Menu> dayEnabledMenu = menuRepo.getBetweenForRestFilteredByEnabled(id, toDay, toDay, true);
        rest.setMenus(dayEnabledMenu);
        return rest;
    }

    @Cacheable("restaurants")
    @Transactional
    public List<Restaurant> getFilteredByNameAndAddressAndEnabledWithDayEnabledMenu(String name, String address, Boolean enabled) {
        log.info("getAllWithDayEnabledMenu restaurants");
        LocalDate toDay = (testDate == null) ? LocalDate.now() : testDate;
        List<Restaurant> allRests = restRepo.getFilteredByNameAndAddressAndEnabled(name, address, enabled, sortByName);

        if (!allRests.isEmpty()) {
            List<Menu> filteredMenus = menuRepo.getAllBetweenFilteredByEnabled(toDay, toDay, true);
            addMenus(allRests, filteredMenus);
        }

        return allRests;
    }

    @Transactional
    @Cacheable("restaurantsEnabled")
    public List<Restaurant> getAllEnabledWithDayEnabledMenu() {
        log.info("getAllEnabledWithDayEnabledMenu restaurants");
        return getFilteredByNameAndAddressAndEnabledWithDayEnabledMenu("", "", true);
    }

    @Transactional
    public List<Restaurant> getAllWithDayEnabledMenu() {
        log.info("getAllWithDayEnabledMenu");
        return getFilteredByNameAndAddressAndEnabledWithDayEnabledMenu("", "", null);
    }

    @Transactional
    @Cacheable("restaurants")
    public void enable(int id, boolean enabled) {
        Restaurant rest = getWithoutMenu(id);
        rest.setEnabled(enabled);
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }
}
