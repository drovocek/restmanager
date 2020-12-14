package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import edu.volkov.restmanager.repository.menuItem.CrudMenuItemRepository;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.model.MenuItemUtil;
import edu.volkov.restmanager.util.model.MenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.util.ValidationUtil.*;

@Service
public class MenuService {

    static final String REST_URL = "/rest/admin/menus";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CrudRestaurantRepository restRepo;
    private final CrudMenuRepository menuRepo;
    private final CrudMenuItemRepository menuItmRepo;

    public MenuService(
            CrudRestaurantRepository restRepo,
            CrudMenuRepository menuRepo,
            CrudMenuItemRepository menuItmRepo
    ) {
        this.restRepo = restRepo;
        this.menuRepo = menuRepo;
        this.menuItmRepo = menuItmRepo;
    }

    @Transactional
    public ResponseEntity<Menu> create(Menu menu, int restId) {
        log.info("\n create menu {} for restaurant: {}", menu, restId);
        checkNew(menu);
        Assert.notNull(menu, "menu must not be null");

        if (!menu.isNew() && getWithMenuItems(menu.getId(), restId) == null) {
            return null;
        }
        menu.setRestaurant(restRepo.getOne(restId));
        Menu createdMenu = menuRepo.save(menu);

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
    public void update(MenuTo menuTo) {
        log.info("\n update menu: {} from to {}", menuTo.id(), menuTo);
        Menu updatedMenu = menuRepo.getWithMenuItems(menuTo.id(), menuTo.getRestId());
        MenuUtil.updateFromTo(updatedMenu, menuTo);
        List<MenuItem> updatedMenuItems = menuItmRepo.getAllByMenuId(menuTo.id());
        MenuItemUtil.updateAllFromTo(updatedMenuItems, menuTo.getMenuItemTos());
    }

    public void delete(int restId, int id) {
        log.info("\n delete menu: {} for rest: {}", id, restId);
        checkNotFoundWithId(menuRepo.delete(id, restId) != 0, restId);
    }

    public Menu getWithMenuItems(int restId, int id) {
        log.info("\n getWithMenuItems menu: {} for rest: {}", id, restId);
        return checkNotFound(
                menuRepo.getWithMenuItems(restId, id),
                "menu by id: " + id + "for restId:" + restId + "dos not exist"
        );
    }

    public List<Menu> getAllForRestWithMenuItems(int restId) {
        log.info("\n getAllForRestWithMenuItems menus");
        return menuRepo.getAllForRestWithMenuItems(restId);
    }

    public List<Menu> getFilteredForRestWithMenuItems(
            int restId,
            LocalDate startDate, LocalDate endDate,
            Boolean enabled
    ) {
        log.info(
                "\n getFilteredForRestWithMenuItems menus for rest: {} ,startDate: {}, endDate: {},enabled: {}",
                restId, startDate, endDate, enabled
        );
        List<Menu> menus = menuRepo.getBetweenForRest(restId, startDate, endDate);
        Predicate<Menu> filter = menu -> enabled == null || menu.isEnabled() == enabled;
        return menus.stream().filter(filter).collect(Collectors.toList());
    }

    @Transactional
    public void enable(int restId, int id, boolean enabled) {
        Menu enabledMenu = getWithMenuItems(restId, id);
        enabledMenu.setEnabled(enabled);
    }
}
