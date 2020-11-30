package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository {
    //ADMIN
    Menu save(Menu menu, int restaurantId);

    boolean delete(int menuId, int restaurantId);

    Menu get(int menuId, int restaurantId);


    List<Menu> getByRestIdBetweenDates(Integer restaurantId, LocalDate startDate, LocalDate endDate);
}
