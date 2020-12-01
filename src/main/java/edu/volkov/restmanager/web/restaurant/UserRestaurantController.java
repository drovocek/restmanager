package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
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
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@RequestMapping("/restaurants")
@Controller
public class UserRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantRepository restRepo;
    private final VoteService voteService;

    public UserRestaurantController(RestaurantRepository restRepo, VoteService voteService) {
        this.restRepo = restRepo;
        this.voteService = voteService;
    }

    @GetMapping("/restaurant")
    public String getEnabled(Integer id, Model model) {
        log.info("getEnabled for restaurant {}", id);
        Restaurant rest = restRepo.getWithDayEnabledMenu(id);
        RestaurantTo restTo = getTo(
                checkNotFoundWithId(
                        rest.isEnabled() ? rest : null,
                        id
                )
        );

        model.addAttribute("restaurant", restTo);
        return "restaurant";
    }

    @GetMapping
    public String getAllEnabled(Model model) {
        log.info("getAllEnabled restaurants");
        Predicate<Restaurant> filter = Restaurant::isEnabled;
        List<RestaurantTo> tos = getFilteredTos(restRepo.getAllWithDayEnabledMenu(), filter);

        model.addAttribute("restaurants", tos);
        return "restaurants";
    }

    @GetMapping("/filter")
    public String getFiltered(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            Model model
    ) {
        log.info("getFiltered restaurants");
        Predicate<Restaurant> filter = getFilterByNameAndAddress(name, address).and(Restaurant::isEnabled);
        List<RestaurantTo> filteredTos = getFilteredTos(restRepo.getAllWithDayEnabledMenu(), filter);

        model.addAttribute("restaurants", filteredTos);
        return "restaurants";
    }

    @GetMapping("/vote")
    public String vote(Integer id) {
        voteService.vote(SecurityUtil.authUserId(), id);
        return "redirect:/restaurants";
    }
}
