package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.util.model.MenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.model.RestaurantUtil.*;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest/any/restaurants";

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    private static final LocalDate TEST_TODAY = LocalDate.of(2020, 1, 27);

    private final CrudRestaurantRepository restRepo;
    private final CrudMenuRepository menuRepo;

    public UserRestaurantController(
            CrudRestaurantRepository restRepo,
            CrudMenuRepository menuRepo
    ) {
        this.restRepo = restRepo;
        this.menuRepo = menuRepo;
    }

    @Transactional
    @GetMapping("/{id}")
    public Restaurant getWithMenu(@PathVariable int id) {
        log.info("\n getWithoutMenu restaurant: {}", id);
        Restaurant rest = restRepo.findById(id).orElse(null);
        checkNotFound(rest, "restaurant by id: " + id + "dos not exist");
        List<Menu> dayEnabledMenu = MenuUtil.filtrate(
                menuRepo.getBetween(TEST_TODAY, TEST_TODAY, id),
                Menu::isEnabled);
        rest.setMenus(dayEnabledMenu);
        return rest;
    }

    @Transactional
    @GetMapping
    public List<Restaurant> getAllEnabledWithDayEnabledMenu() {
        log.info("\n getAllEnabledWithDayEnabledMenu restaurants");
        List<Restaurant> allRests = restRepo.findAll(SORT_NAME);

        if (allRests.isEmpty()) {
            return allRests;
        } else {
            List<Menu> filteredMenus = MenuUtil.filtrate(
                    menuRepo.getAllBetween(TEST_TODAY, TEST_TODAY),
                    Menu::isEnabled
            );
            addMenus(allRests, filteredMenus);

            return filtrate(allRests, Restaurant::isEnabled);
        }
    }

    @Transactional
    @GetMapping("/filter")
    public List<Restaurant> getFilteredEnabledWithDayEnabledMenu(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        log.info("\n getFilteredEnabledWithDayEnabledMenu restaurants by name {} and address {}", name, address);
        Predicate<Restaurant> filter = getFilterByNameAndAddress(name, address).and(Restaurant::isEnabled);
        List<Restaurant> filteredRests = filtrate(restRepo.findAll(SORT_NAME), filter);

        if (filteredRests.isEmpty()) {
            return filteredRests;
        } else {
            List<Menu> filteredMenus = MenuUtil.filtrate(
                    menuRepo.getAllBetween(TEST_TODAY, TEST_TODAY),
                    Menu::isEnabled
            );
            addMenus(filteredRests, filteredMenus);

            return filtrate(filteredRests, Restaurant::isEnabled);
        }
    }
}
