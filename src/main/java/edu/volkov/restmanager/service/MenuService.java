package edu.volkov.restmanager.service;

import com.sun.istack.Nullable;
import edu.volkov.restmanager.View;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.CrudMenuItemRepository;
import edu.volkov.restmanager.repository.CrudMenuRepository;
import edu.volkov.restmanager.repository.CrudRestaurantRepository;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.model.MenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static edu.volkov.restmanager.util.DateTimeUtil.maxIfNull;
import static edu.volkov.restmanager.util.DateTimeUtil.minIfNull;
import static edu.volkov.restmanager.util.ValidationUtil.*;
import static edu.volkov.restmanager.util.model.MenuItemUtil.createNewsFromTos;

@Service
public class MenuService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CrudRestaurantRepository restRepo;
    private final CrudMenuRepository menuRepo;
    private final CrudMenuItemRepository menuItemRepo;

    public MenuService(
            CrudRestaurantRepository restRepo,
            CrudMenuRepository menuRepo,
            CrudMenuItemRepository menuItmRepo
    ) {
        this.restRepo = restRepo;
        this.menuRepo = menuRepo;
        this.menuItemRepo = menuItmRepo;
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public Menu createWithMenuItems(int restId, Menu menu) {
        log.info("create menu {} for restaurant: {}", menu, restId);
        Assert.notNull(menu, "menu must not be null");

        if (!menu.isNew() && getWithMenuItems(menu.getId(), restId) == null) {
            return null;
        }
        menu.setRestaurant(restRepo.getOne(restId));
        validate(menu);
        Menu createdMenu = menuRepo.save(menu);

        if (menu.getMenuItems() != null && !menu.getMenuItems().isEmpty()) {
            List<MenuItem> mis = menu.getMenuItems().stream()
                    .peek(mi -> mi.setMenu(createdMenu))
                    .collect(Collectors.toList());

            menuItemRepo.saveAll(mis);
        }

        return createdMenu;
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void updateWithMenuItems(int restId, int id, MenuTo menuTo) {
        log.info("update menu: {} from to: {} for rest: {}", id, menuTo, restId);

        Menu updated = getWithMenuItems(restId, id);
        if (!updated.getMenuItems().isEmpty()) {
            checkNotFoundWithId(menuItemRepo.deleteAllByMenuId(menuTo.getId()) != 0, (int) menuTo.getId());
        }
        MenuUtil.updateFromTo(updated, menuTo);

        menuItemRepo.saveAll(createNewsFromTos(updated, menuTo.getMenuItemTos()));
    }

    @CacheEvict(value = "menus", allEntries = true)
    public void delete(int restId, int id) {
        log.info("delete menu: {} for rest: {}", id, restId);
        checkNotFoundWithId(menuRepo.delete(id, restId) != 0, restId);
    }

    public Menu getWithMenuItems(int restId, int id) {
        log.info("getWithMenuItems menu: {} for rest: {}", id, restId);
        return checkNotFound(
                menuRepo.getWithMenuItems(restId, id),
                "menu by id: " + id + " for restId: " + restId + " dos not exist"
        );
    }

    @Cacheable("menus")
    public List<Menu> getAllForRestWithMenuItems(int restId) {
        log.info("getAllForRestWithMenuItems menus");
        return menuRepo.getAllForRestWithMenuItems(restId);
    }

    public List<Menu> getFilteredForRestWithMenuItems(
            int restId,
            @Nullable LocalDate startDate, @Nullable LocalDate endDate,
            Boolean enabled
    ) {
        log.info(
                "getFilteredForRestWithMenuItems menus for rest: {} ,startDate: {},endDate: {},enabled: {}",
                restId, startDate, endDate, enabled
        );
        List<Menu> menus = menuRepo.getBetweenForRest(restId, minIfNull(startDate), maxIfNull(endDate));
        Predicate<Menu> filter = menu -> enabled == null || menu.isEnabled() == enabled;
        return menus.stream().filter(filter).collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void enable(int restId, int id, boolean enabled) {
        Menu enabledMenu = getWithMenuItems(restId, id);
        enabledMenu.setEnabled(enabled);
    }

    private void validate(@Validated(View.Web.class) Menu menu){

    }
}
