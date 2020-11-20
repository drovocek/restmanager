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

import static edu.volkov.restmanager.util.RestaurantUtil.getToList;

@RequestMapping("/restaurants")
@Controller
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
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

    @GetMapping
    public String getAllWithDayMenu(
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        model.addAttribute("restaurants", getToList(service.getAllWithDayMenu(date)));
        return "restaurants";
    }

    @GetMapping("/allmenus")
    public String getAllWithoutMenu(Model model) {
        model.addAttribute("restaurants", getToList(service.getAllWithoutMenu()));
        return "restaurants";
    }

    @GetMapping("/vote")
    public String vote(
            @RequestParam(name = "id") Integer id
    ) {
        service.vote(SecurityUtil.authUserId(), id, LocalDate.now());
        return "redirect:/restaurants";
    }
}
