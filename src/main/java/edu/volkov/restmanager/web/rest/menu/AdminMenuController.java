package edu.volkov.restmanager.web.rest.menu;

import com.sun.istack.Nullable;
import edu.volkov.restmanager.HasId;
import edu.volkov.restmanager.View;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.service.MenuService;
import edu.volkov.restmanager.to.MenuTo;
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
import java.time.LocalDate;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.assureIdConsistent;
import static edu.volkov.restmanager.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {

    static final String REST_URL = "/rest/admin/menus";

    private final MenuService service;

    @Qualifier("defaultValidator")
    private Validator validator;

    public AdminMenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping("/{restId}")
    public List<Menu> getAllForRestWithMenuItems(@PathVariable int restId) {
        return service.getAllForRestWithMenuItems(restId);
    }

    @GetMapping("/filter/{restId}")
    public List<Menu> getFilteredForRestWithMenuItems(
            @PathVariable int restId,
            @Nullable LocalDate startDate,
            @Nullable LocalDate endDate,
            @Nullable Boolean enabled
    ) {
        return service.getFilteredForRestWithMenuItems(restId, startDate, endDate, enabled);
    }

    @GetMapping("/{restId}/{id}")
    public Menu getWithMenuItems(@PathVariable int restId, @PathVariable int id) {
        return service.getWithMenuItems(restId, id);
    }

    @PatchMapping("/{restId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(
            @PathVariable int restId,
            @PathVariable int id,
            @RequestParam boolean enabled
    ) {
        service.enable(restId, id, enabled);
    }

    @PostMapping(value = "/{restId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithMenuItems(@PathVariable int restId,  @RequestBody Menu menu) {
        checkNew(menu);
        Menu created = service.createWithMenuItems(restId, menu);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restId}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateWithMenuItems(
            @PathVariable int restId,
            @PathVariable int id,
            @RequestBody MenuTo menuTo
    ) throws BindException {
        validateBeforeUpdate(menuTo, id);
        service.updateWithMenuItems(restId, id, menuTo);
    }

    @DeleteMapping("/{restId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        service.delete(restId, id);
    }

    protected void validateBeforeUpdate(HasId menu, int id) throws BindException {
        assureIdConsistent(menu, id);
        DataBinder binder = new DataBinder(menu);
        binder.addValidators(validator);
        binder.validate(View.Web.class);
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}
