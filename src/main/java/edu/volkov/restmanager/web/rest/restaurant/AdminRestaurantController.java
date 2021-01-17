package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.HasId;
import edu.volkov.restmanager.View;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.to.RestaurantTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.assureIdConsistent;
import static edu.volkov.restmanager.util.ValidationUtil.checkNew;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {

    static final String REST_URL = "/rest/admin/restaurants";

    private final RestaurantService restService;

    private final UniqueRestNameValidator nameValidator;

    @Qualifier("defaultValidator")
    private Validator validator;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = restService.create(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody RestaurantTo restTo, @PathVariable int id) throws BindException {
        validateBeforeUpdate(restTo, id);
        restService.update(restTo, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        restService.delete(id);
    }

    @GetMapping("/{id}")
    public Restaurant getWithoutMenu(@PathVariable int id) {
        return restService.getWithoutMenu(id);
    }

    @GetMapping
    public List<Restaurant> getAllWithDayEnabledMenu() {
        return restService.getFilteredByNameAndAddressAndEnabledWithDayEnabledMenu("","",null);
    }

    @GetMapping("/filter")
    public List<Restaurant> getFilteredWithDayEnabledMenu(String name, String address, Boolean enabled) {
        return restService.getFilteredByNameAndAddressAndEnabledWithDayEnabledMenu(name, address, enabled);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        restService.enable(id, enabled);
    }

    protected void validateBeforeUpdate(HasId rest, int id) throws BindException {
        assureIdConsistent(rest, id);
        DataBinder binder = new DataBinder(rest);
        binder.addValidators(validator, nameValidator);
        binder.validate(View.Web.class);
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}
