package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController {

    static final String REST_URL = "/rest/any/restaurants";

    private final RestaurantService service;

    @GetMapping("/{id}")
    public Restaurant getWithEnabledMenu(@PathVariable int id) {
        return service.getWithEnabledMenu(id);
    }

    @GetMapping
    public List<Restaurant> getAllEnabledWithDayEnabledMenu() {
        return service.getAllEnabledWithDayEnabledMenu();
    }

    @GetMapping("/filter")
    public List<Restaurant> getFilteredEnabledWithDayEnabledMenu(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        return service.getFilteredWithDayEnabledMenu(name, address, true);
    }
}
