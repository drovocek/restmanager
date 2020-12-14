package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.to.RestaurantTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {

    static final String REST_URL = "/rest/admin/restaurants";

    private final RestaurantService service;

    public AdminRestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody RestaurantTo restTo, @PathVariable int id) {
        service.update(restTo, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public Restaurant getWithoutMenu(@PathVariable int id) {
        return service.getWithoutMenu(id);
    }

    @GetMapping
    public List<Restaurant> getAllWithDayEnabledMenu() {
        return service.getAllWithDayEnabledMenu();
    }

    @GetMapping("/filter")
    public List<Restaurant> getFilteredWithDayEnabledMenu(String name, String address, Boolean enabled) {
        return service.getFilteredWithDayEnabledMenu(name, address, enabled);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        service.enable(id, enabled);
    }
}
