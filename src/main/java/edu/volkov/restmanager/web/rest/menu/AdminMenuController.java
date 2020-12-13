package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import edu.volkov.restmanager.repository.menuItem.CrudMenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {

    static final String REST_URL = "/rest/admin/menus";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuRepository menuRepo;
    private final CrudMenuItemRepository menuItmRepo;

    public AdminMenuController(
            MenuRepository repository,
            CrudMenuItemRepository menuItmRepo
    ) {
        this.menuRepo = repository;
        this.menuItmRepo = menuItmRepo;
    }

    @Transactional
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody Menu menu, @PathVariable int id) {
        log.info("\n create menu with to-{}", menu);

        Menu createdMenu = menuRepo.save(menu, id);

        log.info("\n NEW menu - {} \n NEW menuItems - {}", createdMenu, createdMenu.getMenuItems());

        List<MenuItem> mis = menu.getMenuItems().stream()
                .peek(mi -> mi.setMenu(createdMenu))
                .collect(Collectors.toList());

        List<MenuItem> createdMenuItems = menuItmRepo.saveAll(mis);

        createdMenu.setMenuItems(createdMenuItems);

        log.info("\n CREATED menu - {} \n CREATED menuItems - {}", createdMenu, createdMenu.getMenuItems());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(createdMenu.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(createdMenu);
    }

    @GetMapping("/{restId}/{menuId}")
    public Menu get(
            @PathVariable int restId,
            @PathVariable int menuId
    ) {
        log.info("\n get menu: {} for rest: {}", menuId, restId);
        Menu rest = menuRepo.get(menuId, restId);
        return checkNotFound(rest, "menu by menuId: " + menuId + "for restId" + restId + "dos not exist");
    }
//
//    @Transactional
//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    public void update(@RequestBody RestaurantTo restTo) {
//        log.info("\n update restaurant: {}", restTo.id());
//        Restaurant updated = restRepo.findById(restTo.id()).orElse(null);
//        RestaurantUtil.updateFromTo(updated, restTo);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable int id) {
//        log.info("\n delete restaurant: {}", id);
//        checkNotFoundWithId(restRepo.delete(id) != 0, id);
//    }
//
//
//    @Transactional
//    @GetMapping
//    public List<Restaurant> getAllWithDayEnabledMenu() {
//        log.info("\n getAllWithDayEnabledMenu restaurants");
//        List<Restaurant> allRests = restRepo.findAll(SORT_NAME);
//
//        if (allRests.isEmpty()) {
//            return allRests;
//        } else {
//            List<Menu> filteredMenus = filtrate(
//                    menuRepo.getAllBetween(TEST_TODAY, TEST_TODAY),
//                    Menu::isEnabled
//            );
//            addMenus(allRests, filteredMenus);
//
//            return allRests;
//        }
//    }
//
//    @PatchMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
//        Restaurant rest = restRepo.findById(id).orElse(null);
//        checkNotFound(rest, "restaurant by id: " + id + "dos not exist");
//        rest.setEnabled(enabled);
//    }


}
