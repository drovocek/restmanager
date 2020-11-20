package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;

import java.util.List;

public interface MenuRepository {

    Menu save(Menu menu, int restaurantId);

    boolean delete(Integer id);

    Menu get(Integer id);

    List<Menu> getAll();

    List<Menu> getAllByName(String name);
}
