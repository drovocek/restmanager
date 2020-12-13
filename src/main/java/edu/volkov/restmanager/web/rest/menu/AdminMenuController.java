package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import edu.volkov.restmanager.repository.menuItem.CrudMenuItemRepository;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.model.MenuItemUtil;
import edu.volkov.restmanager.util.model.MenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {

    static final String REST_URL = "/rest/admin/menus";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuRepository menuRepo;
    private final CrudMenuRepository crudMenuRepo;
    private final CrudMenuItemRepository menuItmRepo;

    public AdminMenuController(
            MenuRepository repository,
            CrudMenuRepository crudMenuRepo, CrudMenuItemRepository menuItmRepo
    ) {
        this.menuRepo = repository;
        this.crudMenuRepo = crudMenuRepo;
        this.menuItmRepo = menuItmRepo;
    }

    @Transactional
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody Menu menu, @PathVariable int id) {
        log.info("\n create menu with to-{}", menu);

        Menu createdMenu = menuRepo.save(menu, id);

        List<MenuItem> mis = menu.getMenuItems().stream()
                .peek(mi -> mi.setMenu(createdMenu))
                .collect(Collectors.toList());

        List<MenuItem> createdMenuItems = menuItmRepo.saveAll(mis);

        createdMenu.setMenuItems(createdMenuItems);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(createdMenu.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(createdMenu);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody MenuTo menuTo) {
        log.info("\n update menu: {}", menuTo.id());
        Menu updatedMenu = menuRepo.get(menuTo.id(), menuTo.getRestId());
        MenuUtil.updateFromTo(updatedMenu, menuTo);
        List<MenuItem> updatedMenuItems = menuItmRepo.getAllByMenuId(menuTo.id());
        MenuItemUtil.updateAllFromTo(updatedMenuItems, menuTo.getMenuItemTos());
    }

    @GetMapping("/{restId}/{menuId}")
    public Menu get(
            @PathVariable int restId,
            @PathVariable int menuId
    ) {
        log.info("\n get menu: {} for rest: {}", menuId, restId);
        Menu menu = crudMenuRepo.getWithMenuItems(menuId, restId);
        return checkNotFound(menu, "menu by menuId: " + menuId + "for restId" + restId + "dos not exist");
    }

    @DeleteMapping("/{restId}/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable int restId,
            @PathVariable int menuId
    ) {
        log.info("\n delete menu: {} for rest: {}", menuId, restId);
        checkNotFoundWithId(crudMenuRepo.delete(restId, menuId) != 0, restId);
    }

    @Transactional
    @GetMapping("/{restId}")
    public List<Menu> getAll(@PathVariable int restId) {
        log.info("\n getAll menus");
        return crudMenuRepo.getAll(restId);
    }

    @PatchMapping("/{restId}/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(
            @PathVariable int restId,
            @PathVariable int menuId,
            @RequestParam boolean enabled
    ) {
        Menu enabledMenu = crudMenuRepo.findById(menuId)
                .filter(menu -> menu.getRestaurant().getId() == restId)
                .orElse(null);

        checkNotFound(enabledMenu, "menu by menuId: " + menuId + "dos not exist");
        enabledMenu.setEnabled(enabled);
    }
}
