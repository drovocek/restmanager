package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import edu.volkov.restmanager.testdata.MenuItemTestData;
import edu.volkov.restmanager.testdata.MenuTestData;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

import static edu.volkov.restmanager.TestUtil.readFromJson;
import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.MenuTestData.MENU1_ID;
import static edu.volkov.restmanager.testdata.MenuTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.testdata.RestaurantTestData.rest1;
import static edu.volkov.restmanager.testdata.MenuItemTestData.*;
import static edu.volkov.restmanager.testdata.UserTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuRepository menuRepo;

    @Autowired
    private CrudMenuRepository crudMenuRepo;


    @Test
    public void create() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        newMenu.setRestaurant(rest1);
        MenuItem newMenuItm = MenuItemTestData.getNew();
        newMenu.setMenuItems(Arrays.asList(newMenuItm));

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + REST1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated());

        Menu created = readFromJson(action, Menu.class);
        int newMenuId = created.id();
        int newMenuItmId = created.getMenuItems().get(0).getId();
        newMenu.setId(newMenuId);
        newMenuItm.setId(newMenuItmId);

        MENU_WITH_ITEMS_MATCHER.assertMatch(created, newMenu);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menuRepo.get(newMenuId, REST1_ID), newMenu);

    }

    @Test
    public void get() throws Exception {
        Menu menu = new Menu(menu1);
        menu.setMenuItems(menu1MenuItems);

        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID + "/" + MENU1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(menu));
    }
}
