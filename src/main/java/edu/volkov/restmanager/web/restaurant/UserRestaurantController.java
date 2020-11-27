package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.MenuService;
import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.to.RestaurantTo;
import edu.volkov.restmanager.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.RestaurantUtil.*;

@RequestMapping("/restaurants")
@Controller
public class UserRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantService restService;
    private final VoteService voteService;

    public UserRestaurantController(RestaurantService service, VoteService voteService, MenuService menuService) {
        this.restService = service;
        this.voteService = voteService;
    }

    @GetMapping("/restaurant")
    public String getWithDayEnabledMenu(Model model, Integer restId) {
        int userId = SecurityUtil.authUserId();
        log.info("getWithDayEnabledMenu by user {} for restaurant {}", userId, restId);

        model.addAttribute("restaurant", createToWithMenu(restService.getWithDayEnabledMenu(restId)));
        return "restaurant";
    }

    @GetMapping
    public String getAllEnabledWithoutMenu(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAllEnabledWithoutMenu for user {}", userId);

        Predicate<Restaurant> filter = Restaurant::isEnabled;
        List<RestaurantTo> tos = getFilteredTosWithEmptyMenu(restService.getAllWithoutMenu(), filter);

        model.addAttribute("restaurants", tos);
        return "restaurants";
    }

    @GetMapping("/filter")
    public String getFilteredEnabledWithoutMenu(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            Model model
    ) {
        int userId = SecurityUtil.authUserId();
        log.info("getFilteredEnabledWithoutMenu for user {}", userId);

        Predicate<Restaurant> filter = getFilterByNameAndAddress(name, address).and(Restaurant::isEnabled);
        List<RestaurantTo> filteredTos = getFilteredTosWithEmptyMenu(restService.getAllWithoutMenu(), filter);

        model.addAttribute("restaurants", filteredTos);
        return "restaurants";
    }

    @GetMapping("/vote")
    public String vote(Integer id) {
        voteService.vote(SecurityUtil.authUserId(), id);
        return "redirect:/restaurants";
    }
}
