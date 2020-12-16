package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.service.MenuService;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.exception.NotFoundException;
import edu.volkov.restmanager.util.model.MenuUtil;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;

import static edu.volkov.restmanager.TestUtil.readFromJson;
import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.MenuTestData.*;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.admin;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static edu.volkov.restmanager.util.model.MenuUtil.asTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuService service;

    @Test
    public void create() throws Exception {
        Menu newMenu = getNewWithMenuItems();

        ResultActions action = perform(post(REST_URL + "{restId}", REST1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id")
                )));

        Menu created = readFromJson(action, Menu.class);
        int newMenuId = created.id();
        newMenu.setId(newMenuId);

        MENU_WITH_ITEMS_MATCHER.assertMatch(created, newMenu);
        MENU_WITH_ITEMS_MATCHER.assertMatch(service.getWithMenuItems(REST1_ID, newMenuId), newMenu);
    }

    @Test
    public void updateWithMenuItems() throws Exception {
        MenuTo updatedMenuTo = asTo(getUpdatedWithMenuItems());

        perform(put(REST_URL + "{restId}/{id}", REST1_ID, MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));

        MENU_WITH_ITEMS_MATCHER.assertMatch(
                service.getWithMenuItems(REST1_ID, MENU1_ID),
                MenuUtil.updateFromTo(new Menu(menu1), asTo(getUpdatedWithMenuItems())));
    }

    @Test
    public void updateEmptyMenuItems() throws Exception {
        MenuTo updatedMenuTo = asTo(getUpdatedWithMenuItems());
        updatedMenuTo.setMenuItemTos(Collections.emptyList());

        perform(put(REST_URL + "{restId}/{id}", REST1_ID, MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));

        Menu updatedMenu = getUpdatedWithMenuItems();
        updatedMenu.setMenuItems(Collections.emptyList());

        MENU_WITH_ITEMS_MATCHER.assertMatch(service.getWithMenuItems(REST1_ID, MENU1_ID), updatedMenu);
    }

    @Test
    public void deleteGood() throws Exception {
        perform(delete(REST_URL + "{restId}/{id}", REST1_ID, MENU1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));

        assertThrows(NotFoundException.class, () -> service.getWithMenuItems(REST1_ID, MENU1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(delete(REST_URL + "{restId}/{id}", REST1_ID, MENU_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(document("{class-name}/{method-name}"));
    }

    @Test
    public void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden())
                .andDo(document("{class-name}/{method-name}"));
    }

    @Test
    public void getWithMenuItems() throws Exception {
        perform(get(REST_URL + "{restId}/{id}", REST1_ID, MENU1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(menu1WithItems))
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));
    }

    @Test
    public void getWithMenuItemsNotFound() throws Exception {
        perform(get(REST_URL + "{restId}/{id}", REST1_ID, MENU_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));
    }

    @Test
    public void getAllForRestWithMenuItems() throws Exception {
        perform(get(REST_URL + "{restId}", REST1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(rest1AllMenusWithItems))
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id")
                )));
    }

    @Test
    public void getFilteredForRest1AllWithMenuItems() throws Exception {
        perform(get(REST_URL + "filter/{restId}", REST1_ID)
                .with(userHttpBasic(admin))
                .param("startDate", "")
                .param("endDate", "")
                .param("enabled", ""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(rest1AllMenusWithItems))
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id")
                )));
    }

    @Test
    public void getFilteredForRest1EnabledWithMenuItems() throws Exception {
        perform(get(REST_URL + "filter/{restId}", REST1_ID)
                .with(userHttpBasic(admin))
                .param("startDate", "")
                .param("endDate", "")
                .param("enabled", "true"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(
                        Arrays.asList(menu3WithItems, menu2WithItems)
                ))
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id")
                )));
    }

    @Test
    public void getFilteredForRest1TodayWithMenuItems() throws Exception {
        perform(get(REST_URL + "filter/{restId}", REST1_ID)
                .with(userHttpBasic(admin))
                .param("startDate", TODAY.toString())
                .param("endDate", TODAY.toString())
                .param("enabled", ""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(
                        Arrays.asList(menu1WithItems, menu2WithItems)
                ))
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id")
                )));
    }

    @Test
    public void enable() throws Exception {
        perform(patch(REST_URL + "{restId}/{id}", REST1_ID, MENU1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("restId").description("Restaurant id"),
                        parameterWithName("id").description("Menu id")
                )));

        assertFalse(service.getWithMenuItems(REST1_ID, MENU1_ID).isEnabled());
    }
}
