package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository {

    //USER
    List<Menu> getBetween(LocalDate startDate, LocalDate endDate);

    //ADMIN
    Menu save(Menu menu, int restaurantId);

    boolean delete(Integer id);

    Menu get(Integer id);

    List<Menu> getAll();

    List<Menu> getAllByName(String name);
}
