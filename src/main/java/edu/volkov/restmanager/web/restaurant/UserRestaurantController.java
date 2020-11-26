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

import static edu.volkov.restmanager.util.RestaurantUtil.getFilterByNameAndAddress;
import static edu.volkov.restmanager.util.RestaurantUtil.getFilteredTos;

@RequestMapping("/restaurants")
@Controller
public class UserRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantService restaurantService;
    private final VoteService voteService;

    public UserRestaurantController(RestaurantService service, VoteService voteService, MenuService menuService) {
        this.restaurantService = service;
        this.voteService = voteService;
    }

    @GetMapping
    public String getAllEnabled(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getFilteredByNameAndAddressWithEnabledMenu for user {}", userId);

        Predicate<Restaurant> filter = restaurant -> true;
        List<RestaurantTo> tos = getFilteredTos(restaurantService.getAllWithDayEnabledMenu(), filter);

        model.addAttribute("restaurants", tos);
        return "restaurants";
    }

    @GetMapping("/filter")
    public String getFilteredEnabled(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            Model model
    ) {
        int userId = SecurityUtil.authUserId();
        log.info("getFilteredByNameAndAddressWithEnabledMenu for user {}", userId);

        Predicate<Restaurant> filter = getFilterByNameAndAddress(name, address).and(Restaurant::isEnabled);
        List<RestaurantTo> filteredTo = getFilteredTos(restaurantService.getAllWithDayEnabledMenu(), filter);

        model.addAttribute("restaurants", filteredTo);
        return "restaurants";
    }

    @GetMapping("/vote")
    public String vote(Integer id) {
        voteService.vote(SecurityUtil.authUserId(), id);
        return "redirect:/restaurants";
    }
}
