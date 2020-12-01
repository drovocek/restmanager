package edu.volkov.restmanager.repository.menuItem;

import edu.volkov.restmanager.model.MenuItem;

public interface MenuItemRepository {
    // null if updated mItem do not belong to menuId
    MenuItem save(MenuItem mItem, int menuId);

    // false if menuItem do not belong to menuId
    boolean delete(int id, int menuId);

    // null if menuItem do not belong to menuId
    MenuItem get(int id, int menuId);
}
