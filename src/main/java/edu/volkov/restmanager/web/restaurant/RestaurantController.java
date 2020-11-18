package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static edu.volkov.restmanager.util.RestaurantUtil.getToList;

@RequestMapping("/restaurants")
@Controller
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public String save(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "phone") String phone
    ) {
        Restaurant restaurant = new Restaurant(id, name, address, phone);

        if (restaurant.isNew()) {
            restaurantService.create(restaurant);
        } else {
            restaurantService.update(restaurant);
        }

        return "redirect:/restaurants";
    }

    @GetMapping("/restaurantForm")
    public String getRestaurantForm(
            @RequestParam(name = "id", required = false) Integer id,
            Model model
    ) {
        Restaurant restaurant = (id == null)
                ? new Restaurant("", "", "+7(495) 000-0000")
                : restaurantService.get(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurantForm";
    }

    @GetMapping("/delete")
    public String erase(@RequestParam(name = "id") Integer id) {
        restaurantService.delete(id);
        return "redirect:/restaurants";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("restaurants", getToList(restaurantService.getAll()));
        return "restaurants";
    }
}
