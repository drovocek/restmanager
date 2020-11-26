package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static edu.volkov.restmanager.util.DateTimeUtil.maxIfNull;
import static edu.volkov.restmanager.util.DateTimeUtil.minIfNull;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {

    private final MenuRepository repository;
    private int dayMenuQuantity = 1;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    //USER
    public List<Menu> getByPreassignedQuantity() {
        //TODO now()
//        LocalDate startDay = LocalDate.now();
        LocalDate startDate = LocalDate.of(2020, 1, 27);
        LocalDate endDate = startDate.plus(dayMenuQuantity - 1, ChronoUnit.DAYS);
        return getBetween(startDate, endDate);
    }

    public List<Menu> getBetween(LocalDate startDate, LocalDate endDate) {
        return repository.getBetween(minIfNull(startDate), maxIfNull(endDate));
    }


    //ADMIN
    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must be not null");
        return repository.save(menu, restaurantId);
    }

    public void update(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must be not null");
        checkNotFoundWithId(repository.save(menu, restaurantId), menu.getId());
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Menu get(Integer id) {
        return checkNotFound(repository.get(id), "menu by id: " + id + "dos not exist");
    }

    public List<Menu> getAll() {
        return repository.getAll();
    }

    public List<Menu> getAllByName(String name) {
        return checkNotFound(repository.getAllByName(name), "menu by id: " + name + "dos not exist");
    }

    public void setDayMenuQuantity(int dayMenuQuantity) {
        if (dayMenuQuantity > 0) {
            this.dayMenuQuantity = dayMenuQuantity;
        }
    }

    //    public List<Menu> getFilteredByEnabledBetweenDatesWithRestaurant(boolean enabled, LocalDate startDate, LocalDate endDate) {
//        return repository.getFilteredByEnabledBetweenDatesWithRestaurant(true, startDate, endDate);
//    }
}
