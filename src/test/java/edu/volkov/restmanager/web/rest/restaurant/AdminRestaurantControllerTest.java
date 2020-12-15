package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.testdata.MenuTestData;
import edu.volkov.restmanager.testdata.RestaurantTestData;
import edu.volkov.restmanager.to.RestaurantTo;
import edu.volkov.restmanager.util.exception.NotFoundException;
import edu.volkov.restmanager.util.model.RestaurantUtil;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static edu.volkov.restmanager.TestUtil.readFromJson;
import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static edu.volkov.restmanager.testdata.UserTestData.admin;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static edu.volkov.restmanager.util.model.RestaurantUtil.asTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    protected RestaurantService service;

    @Test
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> service.getWithoutMenu(REST1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void update() throws Exception {
        RestaurantTo updatedTo = asTo(getUpdated());
        perform(MockMvcRequestBuilders.put(REST_URL + REST1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent());

        REST_MATCHER.assertMatch(
                service.getWithoutMenu(REST1_ID),
                RestaurantUtil.updateFromTo(new Restaurant(rest1), updatedTo)
        );
    }

    @Test
    public void create() throws Exception {
        Restaurant newRest = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newRest)))
                .andExpect(status().isCreated());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRest.setId(newId);
        REST_MATCHER.assertMatch(created, newRest);
        REST_MATCHER.assertMatch(service.getWithoutMenu(newId), newRest);
    }

    @Test
    public void getWithoutMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_MATCHER.contentJson(rest1));
    }

    @Test
    public void getWithoutMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getAllWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        rest1WithDayEnabledMenusAndItems,
                        rest2WithDayEnabledMenusAndItems)
                );
    }

    @Test
    public void getFilteredWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter/")
                .param("name", "rest1")
                .param("address", "address1")
                .param("enabled", "")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        Collections.singletonList(rest1WithDayEnabledMenusAndItems)));
    }

    @Test
    public void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + REST1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(service.getWithoutMenu(REST1_ID).isEnabled());
    }
}