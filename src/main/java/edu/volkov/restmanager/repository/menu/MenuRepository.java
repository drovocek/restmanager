package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository {
    // null if updated menu do not belong to restId
    Menu save(Menu menu, int restId);

    // false if menu do not belong to restId
    boolean delete(int id, int restId);

    // null if menu do not belong to restId
    Menu get(int id, int restId);

    // ORDERED date desc
    List<Menu> getAll(int restId);

    // ORDERED date desc
    List<Menu> getBetween(LocalDate startDate, LocalDate endDate, int restId);
}