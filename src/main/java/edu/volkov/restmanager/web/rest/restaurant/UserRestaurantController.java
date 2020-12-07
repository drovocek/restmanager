package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.util.model.MenuUtil;
import edu.volkov.restmanager.util.model.RestaurantUtil;
import edu.volkov.restmanager.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.model.RestaurantUtil.addMenus;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController {

    static final String REST_URL = "/rest/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CrudRestaurantRepository restRepo;
    private final CrudMenuRepository menuRepo;
    private final VoteService voteService;

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    private static final LocalDate TEST_TODAY = LocalDate.of(2020, 1, 27);

    public UserRestaurantController(
            CrudRestaurantRepository restRepo,
            CrudMenuRepository menuRepo,
            VoteService voteService
    ) {
        this.restRepo = restRepo;
        this.menuRepo = menuRepo;
        this.voteService = voteService;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("\n vote user:{} by restaurant:{}", userId, id);
        //TODO throw limit Exspn
        voteService.vote(userId, id);
    }

    @GetMapping("/{id}")
    public Restaurant getWithMenu(@PathVariable int id) {
        log.info("\n getWithoutMenu restaurant: {}", id);
        Restaurant rest = restRepo.findById(id).orElse(null);
        checkNotFound(rest, "restaurant by id: " + id + "dos not exist");
        List<Menu> dayMenu = menuRepo.getBetween(TEST_TODAY, TEST_TODAY, id);
        rest.setMenus(dayMenu);
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

            return RestaurantUtil.filtrate(allRests, Restaurant::isEnabled);
        }
    }
}
