package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {

    private final MenuRepository repository;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    //USER

    //ADMIN
    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must be not null");
        return repository.save(menu, restaurantId);
    }

    public void update(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must be not null");
        checkNotFoundWithId(repository.save(menu, restaurantId), menu.getId());
    }

    public void delete(int menuId, int restaurantId) {
        checkNotFoundWithId(repository.delete(menuId, restaurantId), menuId);
    }

    public Menu get(Integer id) {
        return checkNotFound(repository.get(id), "menu by id: " + id + "dos not exist");
    }

    public List<Menu> getByRestIdBetweenDates(Integer restaurantId, LocalDate startDate, LocalDate endDate) {
        return repository.getByRestIdBetweenDates(restaurantId, startDate, endDate);
    }

    //
    public List<Menu> getAll() {
        return repository.getAll();
    }
}
