package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository {

    Menu save(Menu menu, int restaurantId);

    boolean delete(Integer id);

    Menu get(Integer id);

    List<Menu> getAll();

    List<Menu> getFilteredByEnabledBetweenDates(boolean enabled, LocalDate startDate, LocalDate endDate);

    List<Menu> getAllByName(String name);
}
