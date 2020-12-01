package edu.volkov.restmanager.repository.menuItem;

import edu.volkov.restmanager.model.MenuItem;

public interface MenuItemRepository {

    MenuItem save(MenuItem mItem, int menuId);

    boolean delete(int id, int menuId);

    MenuItem get(int id, int menuId);
}
