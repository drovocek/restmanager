package edu.volkov.restmanager.web.rest.menu;

import com.sun.istack.Nullable;
import edu.volkov.restmanager.View;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.service.MenuService;
import edu.volkov.restmanager.to.MenuTo;
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
import java.time.LocalDate;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.assureIdConsistent;
import static edu.volkov.restmanager.util.ValidationUtil.checkNew;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {

    static final String REST_URL = "/rest/admin/menus";

    private final MenuService service;

    private final UniqueMenuInOneDateForRestValidator menuInOneDateForRestValidator;

    @Qualifier("defaultValidator")
    private Validator validator;

    @GetMapping("/")
    public List<Menu> getAllForRestWithMenuItems(@RequestParam int restId) {
        return service.getAllForRestWithMenuItems(restId);
    }

    @GetMapping("/filter")
    public List<Menu> getFilteredForRestWithMenuItems(
            @RequestParam int restId,
            @Nullable LocalDate startDate,
            @Nullable LocalDate endDate,
            @Nullable Boolean enabled
    ) {
        return service.getFilteredForRestWithMenuItems(restId, startDate, endDate, enabled);
    }

    @GetMapping("/{id}")
    public Menu getWithMenuItems(@RequestParam int restId, @PathVariable int id) {
        return service.getWithMenuItems(restId, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(
            @RequestParam int restId,
            @PathVariable int id,
            @RequestParam boolean enabled
    ) {
        service.enable(restId, id, enabled);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithMenuItems(@RequestParam int restId, @Validated(View.Web.class) @RequestBody Menu menu) {
        checkNew(menu);
        Menu created = service.createWithMenuItems(restId, menu);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restId}/{id}")
                .buildAndExpand(restId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateWithMenuItems(
            @RequestParam int restId,
            @PathVariable int id,
            @RequestBody MenuTo menuTo
    ) throws BindException {
        menuTo.setRestId(restId);
        validateBeforeUpdate(menuTo, id);
        service.updateWithMenuItems(restId, id, menuTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam int restId, @PathVariable int id) {
        service.delete(restId, id);
    }

    protected void validateBeforeUpdate(MenuTo menuTo, int id) throws BindException {
        assureIdConsistent(menuTo, id);
        DataBinder binder = new DataBinder(menuTo);
        binder.addValidators(validator, menuInOneDateForRestValidator);
        binder.validate(View.Web.class);
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}
