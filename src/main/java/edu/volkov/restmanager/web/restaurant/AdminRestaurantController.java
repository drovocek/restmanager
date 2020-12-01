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

import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.RestaurantUtil.getFilteredTos;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;


@RequestMapping("/restaurantsManaging")
@Controller
public class AdminRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantRepository restRepo;

    public AdminRestaurantController(RestaurantRepository restRepo) {
        this.restRepo = restRepo;
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
            log.info("create restaurant");
            restRepo.save(restaurant);
        } else {
            log.info("update restaurant");
            checkNotFoundWithId(restRepo.save(restaurant), restaurant.getId());
        }

        return "redirect:/restaurantsManaging";
    }

    @GetMapping("/form")
    public String updateOrCreate(
            @RequestParam(required = false) Integer id,
            Model model
    ) {
        log.info("updateOrCreate for restaurant {}", id);
        Restaurant restaurant = (id == null)
                ? new Restaurant("", "", "+7(495) 000-0000")
                : restRepo.get(id);

        model.addAttribute("restaurant", restaurant);
        return "restaurantForm";
    }

    @GetMapping("/delete")
    public String erase(int id) {
        log.info("erase for restaurant {}", id);
        checkNotFoundWithId(restRepo.delete(id), id);
        return "redirect:/restaurantsManaging";
    }

    @GetMapping
    public String getAll(Model model) {
        log.info("getAll restaurants");
        Predicate<Restaurant> filter = rest -> true;
        List<RestaurantTo> tos = getFilteredTos(restRepo.getAllWithDayEnabledMenu(), filter);

        model.addAttribute("restaurants", tos);
        return "restaurantsManaging";
    }
}
