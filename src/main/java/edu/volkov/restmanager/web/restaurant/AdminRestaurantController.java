package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.restaurant.RestaurantRepository;
import edu.volkov.restmanager.to.RestaurantTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;
import static edu.volkov.restmanager.util.model.RestaurantUtil.createTo;
import static edu.volkov.restmanager.util.model.RestaurantUtil.getFilteredTos;


@RequestMapping("/restaurantsManaging")
@Controller
public class AdminRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantRepository repository;

    public AdminRestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public String save(
            @RequestParam(required = false) Integer id,
            String name,
            String address,
            String phone,
            Boolean enabled
    ) {
        Restaurant restaurant = new Restaurant(id, name, address, phone, enabled, 0);

        if (restaurant.isNew()) {
            log.info("\n create restaurant");
            repository.save(restaurant);
        } else {
            log.info("\n update restaurant");
            checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
        }

        return "redirect:/restaurantsManaging";
    }

    @GetMapping("/update")
    public String update(Integer id, Model model) {
        RestaurantTo to = createTo(checkNotFound(repository.get(id), "restaurant by id: " + id + "dos not exist"));
        model.addAttribute("menuTo", to);
        return "restaurantForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        RestaurantTo toDef = new RestaurantTo(null, "def", "", "+7(495) 000-0000", 0, Collections.emptyList());
        model.addAttribute("restTo", toDef);
        return "restaurantForm";
    }

    @GetMapping("/delete")
    public String erase(int id) {
        log.info("\n erase for restaurant {}", id);
        checkNotFoundWithId(repository.delete(id), id);
        return "redirect:/restaurantsManaging";
    }

    @GetMapping
    public String getAll(Model model) {
        log.info("\n getAll restaurants");
        Predicate<Restaurant> filter = rest -> true;
        List<RestaurantTo> tos = getFilteredTos(repository.getAllWithDayEnabledMenu(), filter);

        model.addAttribute("restaurants", tos);
        return "restaurantsManaging";
    }
}
