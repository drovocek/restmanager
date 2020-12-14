package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.AbstractTest;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menuItem.CrudMenuItemRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static edu.volkov.restmanager.testdata.MenuItemTestData.*;
import static edu.volkov.restmanager.testdata.MenuTestData.MENU1_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MenuItemRepositoryTest extends AbstractTest {

//    @Autowired
//    private CrudMenuItemRepository repository;
//
//    @Test
//    public void create() {
//        MenuItem created = repository.save(getNew(), MENU1_ID);
//        int newId = created.id();
//        MenuItem newMenuItem = getNew();
//        newMenuItem.setId(newId);
//        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
//        MENU_ITEM_MATCHER.assertMatch(repository.get(newId, MENU1_ID), newMenuItem);
//    }
//
//    @Test
//    public void delete() {
//        repository.delete(MENU_ITEM1_ID, MENU1_ID);
//        assertNull(repository.get(MENU_ITEM1_ID, MENU1_ID));
//    }
//
//    @Test
//    public void deletedNotFound() {
//        assertFalse(repository.delete(MENU_ITEM1_ID, MENU1_ID + 1));
//    }
//
//    @Test
//    public void get() {
//        MenuItem menuItm = repository.get(MENU_ITEM1_ID, MENU1_ID);
//        MENU_ITEM_MATCHER.assertMatch(menuItm, menuItem1);
//    }
//
//    @Test
//    public void getNotFound() {
//        assertNull(repository.get(MENU_ITEM_NOT_FOUND_ID, MENU1_ID));
//    }
//
//    @Test
//    public void update() {
//        MenuItem updated = getUpdated();
//        repository.save(updated, MENU1_ID);
//        MenuItem actual = repository.get(MENU_ITEM1_ID, MENU1_ID);
//        MENU_ITEM_MATCHER.assertMatch(actual, getUpdated());
//    }
//
//    @Test
//    public void updateNotFound() {
//        MenuItem updated = getUpdated();
//        assertNull(repository.save(updated, MENU1_ID + 1));
//    }
}
