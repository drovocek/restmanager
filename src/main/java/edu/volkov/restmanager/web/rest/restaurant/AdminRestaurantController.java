package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.to.RestaurantTo;
import edu.volkov.restmanager.util.model.RestaurantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;
import static edu.volkov.restmanager.util.model.MenuUtil.filtrate;
import static edu.volkov.restmanager.util.model.RestaurantUtil.addMenus;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {

    static final String REST_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CrudRestaurantRepository restRepo;
    private final CrudMenuRepository menuRepo;

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    private static final LocalDate TEST_TODAY = LocalDate.of(2020, 1, 27);

    public AdminRestaurantController(
            CrudRestaurantRepository restRepo,
            CrudMenuRepository menuRepo
    ) {
        this.restRepo = restRepo;
        this.menuRepo = menuRepo;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        log.info("\n create restaurant");
        Restaurant created = restRepo.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody RestaurantTo restTo) {
        log.info("\n update restaurant: {}", restTo.id());
        Restaurant updated = restRepo.findById(restTo.id()).orElse(null);
        RestaurantUtil.updateFromTo(updated, restTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("\n delete restaurant: {}", id);
        checkNotFoundWithId(restRepo.delete(id) != 0, id);
    }

    @GetMapping("/{id}")
    public Restaurant getWithoutMenu(@PathVariable int id) {
        log.info("\n getWithoutMenu restaurant: {}", id);
        Restaurant rest = restRepo.findById(id).orElse(null);
        return checkNotFound(rest, "restaurant by id: " + id + "dos not exist");
    }

    @Transactional
    @GetMapping
    public List<Restaurant> getAllWithDayEnabledMenu() {
        log.info("\n getAllWithDayEnabledMenu restaurants");
        List<Restaurant> allRests = restRepo.findAll(SORT_NAME);

        if (allRests.isEmpty()) {
            return allRests;
        } else {
            List<Menu> filteredMenus = filtrate(
                    menuRepo.getAllBetween(TEST_TODAY, TEST_TODAY),
                    Menu::isEnabled
            );
            addMenus(allRests, filteredMenus);

            return allRests;
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        Restaurant rest = restRepo.findById(id).orElse(null);
        checkNotFound(rest, "restaurant by id: " + id + "dos not exist");
        rest.setEnabled(enabled);
    }
}
