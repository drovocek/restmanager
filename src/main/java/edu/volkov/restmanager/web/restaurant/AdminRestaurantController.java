package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
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

import static edu.volkov.restmanager.util.RestaurantUtil.getFilteredTosWithEmptyMenu;


@RequestMapping("/restaurantsManaging")
@Controller
public class AdminRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantService service;

    public AdminRestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping
    public String save(
            @RequestParam(name = "id", required = false) Integer id,
            String name,
            String address,
            String phone,
            Boolean enabled
    ) {
        Restaurant restaurant = new Restaurant(id, name, address, phone, enabled, 0);

        if (restaurant.isNew()) {
            service.create(restaurant);
        } else {
            service.update(restaurant);
        }

        return "redirect:/restaurantsManaging";
    }

    @GetMapping("/form")
    public String updateOrCreate(@RequestParam(required = false) Integer id, Model model) {
        Restaurant restaurant = (id == null)
                ? new Restaurant("", "", "+7(495) 000-0000")
                : service.get(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurantForm";
    }

    @GetMapping("/delete")
    public String erase(Integer id) {
        service.delete(id);
        return "redirect:/restaurantsManaging";
    }

    @GetMapping
    public String getAllWithoutMenu(Model model) {
        Predicate<Restaurant> filter = restaurant -> true;
        List<RestaurantTo> tos = getFilteredTosWithEmptyMenu(service.getAllWithoutMenu(), filter);

        model.addAttribute("restaurants", tos);
        return "restaurantsManaging";
    }
}
