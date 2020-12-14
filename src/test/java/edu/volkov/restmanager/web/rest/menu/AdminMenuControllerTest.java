package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.testdata.MenuItemTestData;
import edu.volkov.restmanager.testdata.MenuTestData;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.model.MenuUtil;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static edu.volkov.restmanager.TestUtil.readFromJson;
import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.MenuItemTestData.menuItem2;
import static edu.volkov.restmanager.testdata.MenuItemTestData.menuItem3;
import static edu.volkov.restmanager.testdata.MenuTestData.MENU1_ID;
import static edu.volkov.restmanager.testdata.MenuTestData.MENU_WITH_ITEMS_MATCHER;
import static edu.volkov.restmanager.testdata.MenuTestData.rest1AllMenusWithItems;
import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static edu.volkov.restmanager.testdata.UserTestData.admin;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminMenuControllerTest extends AbstractControllerTest {

//    private static final String REST_URL = AdminMenuController.REST_URL + '/';
//
//    @Autowired
//    private MenuRepository menuRepo;
//
//    @Test
//    public void create() throws Exception {
//        Menu newMenu = MenuTestData.getNew();
//        newMenu.setRestaurant(rest1);
//        MenuItem newMenuItm = MenuItemTestData.getNew();
//        newMenu.setMenuItems(Arrays.asList(newMenuItm));
//
//        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + REST1_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(admin))
//                .content(JsonUtil.writeValue(newMenu)))
//                .andExpect(status().isCreated());
//
//        Menu created = readFromJson(action, Menu.class);
//        int newMenuId = created.id();
//        int newMenuItmId = created.getMenuItems().get(0).getId();
//        newMenu.setId(newMenuId);
//        newMenuItm.setId(newMenuItmId);
//
//        MENU_WITH_ITEMS_MATCHER.assertMatch(created, newMenu);
//        MENU_WITH_ITEMS_MATCHER.assertMatch(menuRepo.get(newMenuId, REST1_ID), newMenu);
//
//    }
//
//    @Test
//    public void update() throws Exception {
//        Menu updatedMenu = MenuTestData.getUpdated();
//        updatedMenu.setRestaurant(rest1);
//        MenuItem updatedMenuItm = MenuItemTestData.getUpdated();
//        updatedMenuItm.setMenu(updatedMenu);
//        updatedMenu.setMenuItems(Arrays.asList(updatedMenuItm));
//        MenuTo updatedMenuTo = MenuUtil.asTo(updatedMenu);
//
//        perform(MockMvcRequestBuilders.put(REST_URL + REST1_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(admin))
//                .content(JsonUtil.writeValue(updatedMenuTo)))
//                .andExpect(status().isNoContent());
//
//        List<MenuItem> updatedMenuItems = Arrays.asList(updatedMenuItm, menuItem2, menuItem3);
//        Menu updated = MenuUtil.updateFromTo(updatedMenu, updatedMenuTo);
//        updated.setMenuItems(updatedMenuItems);
//
//        MENU_WITH_ITEMS_MATCHER.assertMatch(menuRepo.get(MENU1_ID, REST1_ID), updated);
//    }
//
//    @Test
//    public void delete() throws Exception {
//        perform(MockMvcRequestBuilders.delete(REST_URL + REST1_ID + "/" + MENU1_ID)
//                .with(userHttpBasic(admin)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//
//        assertNull(menuRepo.get(MENU1_ID, REST1_ID));
//    }
//
//
//    @Test
//    public void getAll() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL+ REST1_ID)
//                .with(userHttpBasic(admin)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(
//                        rest1AllMenusWithItems)
//                );
//    }
//
//    @Test
//    public void getBetween() throws Exception {
//
//    }
//
//    @Test
//    public void getUnAuth() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void getForbidden() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL)
//                .with(userHttpBasic(user1)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void enable() throws Exception {
//        perform(MockMvcRequestBuilders.patch(REST_URL + REST1_ID + "/" + MENU1_ID)
//                .param("enabled", "false")
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(admin)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//        assertFalse(menuRepo.get(MENU1_ID, REST1_ID).isEnabled());
//    }
}
