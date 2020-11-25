package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.web.SecurityUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;



@RequestMapping("/restaurantsManaging")
@Controller
public class AdminRestaurantController {

    private final RestaurantService service;

    public AdminRestaurantController(RestaurantService service) {
        this.service = service;
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
            service.create(restaurant);
        } else {
            service.update(restaurant);
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
                : service.get(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurantForm";
    }

    @GetMapping("/delete")
    public String erase(@RequestParam(name = "id") Integer id) {
        service.delete(id);
        return "redirect:/restaurants";
    }
//
//    @GetMapping("/allmenus")
//    public String getAllWithoutMenu(Model model) {
//        model.addAttribute("restaurants", getTo(service.getAllWithoutMenu()));
//        return "restaurants";
//    }
}
