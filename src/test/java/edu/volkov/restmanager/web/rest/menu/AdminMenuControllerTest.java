package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.service.MenuService;
import edu.volkov.restmanager.to.MenuItemTo;
import edu.volkov.restmanager.to.MenuTo;
import edu.volkov.restmanager.util.exception.ErrorType;
import edu.volkov.restmanager.util.exception.NotFoundException;
import edu.volkov.restmanager.util.model.MenuUtil;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultActions;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"))
                        )
                )
                .andDo(getRequestParamDocForOneMenu())
                .andDo(getResponseParamDocForOneMenu());

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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id")
                        )
                ))
                .andDo(getRequestParamDocForOneMenuTo());

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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id"))
                ));

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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id")
                        )
                ))
                .andDo(getErrorResponseParamDoc());
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(document("{class-name}/{method-name}"));
    }

    @Test
    public void getForbidden() throws Exception {
        perform(get(REST_URL)
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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id"))
                        )
                )
                .andDo(getResponseParamDocForOneMenu());
    }

    @Test
    public void getWithMenuItemsNotFound() throws Exception {
        perform(get(REST_URL + "{restId}/{id}", REST1_ID, MENU_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id"))
                        )
                )
                .andDo(getErrorResponseParamDoc());
    }

    @Test
    public void getAllForRestWithMenuItems() throws Exception {
        perform(get(REST_URL + "{restId}", REST1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_ITEMS_MATCHER.contentJson(rest1AllMenusWithItems))
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"))
                        )
                )
                .andDo(getResponseParamDocForManyMenu());
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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"))
                        )
                )
                .andDo(document("{class-name}/{method-name}",
                        requestParameters(
                                parameterWithName("startDate").description("Start date"),
                                parameterWithName("endDate").description("End date"),
                                parameterWithName("enabled").description("Menu activity marker")
                        )
                        )
                )
                .andDo(getResponseParamDocForManyMenu());
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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"))
                        )
                )
                .andDo(getResponseParamDocForManyMenu());
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
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"))
                        )
                )
                .andDo(getResponseParamDocForManyMenu());
    }

    @Test
    public void enable() throws Exception {
        perform(patch(REST_URL + "{restId}/{id}", REST1_ID, MENU1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id"))
                        )
                )
                .andDo(document("{class-name}/{method-name}",
                        requestParameters(
                                parameterWithName("enabled").description("Menu activity marker"))
                ));

        assertFalse(service.getWithMenuItems(REST1_ID, MENU1_ID).isEnabled());
    }

    @Test
    public void enableNotFound() throws Exception {
        perform(patch(REST_URL + "{restId}/{id}", REST1_ID, MENU_NOT_FOUND_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id"),
                                parameterWithName("id").description("Menu id")
                        )
                ))
                .andDo(getErrorResponseParamDoc()).andDo(document("{class-name}/{method-name}",
                requestParameters(
                        parameterWithName("enabled").description("Menu activity marker"))
        ));
    }

    private RestDocumentationResultHandler getResponseParamDocForOneMenu() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("id").description("Menu id"),
                        fieldWithPath("name").description("Menu name"),
                        fieldWithPath("menuDate").description("Menu date"),
                        fieldWithPath("enabled").description("Menu activity marker"),
                        subsectionWithPath("menuItems").description("Menu dishes"),
                        fieldWithPath("menuItems[].id").description("Dish id"),
                        fieldWithPath("menuItems[].name").description("Dish name"),
                        fieldWithPath("menuItems[].price").description("Dish price")
                ));
    }

    private RestDocumentationResultHandler getRequestParamDocForOneMenu() {

        ConstraintDescriptions constraintDescMenu = new ConstraintDescriptions(Menu.class);
        ConstraintDescriptions constraintDescMenuItem = new ConstraintDescriptions(MenuItem.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("name").description("Menu name")
                                .attributes(key("constraints").value(constraintDescMenu.descriptionsForProperty("name"))),
                        fieldWithPath("menuDate").description("Menu date")
                                .attributes(key("constraints").value(constraintDescMenu.descriptionsForProperty("menuDate"))),
                        fieldWithPath("enabled").description("Menu activity marker")
                                .attributes(key("constraints").value(constraintDescMenu.descriptionsForProperty("enabled"))),
                        subsectionWithPath("menuItems").description("Menu dishes")
                                .attributes(key("constraints").value(constraintDescMenu.descriptionsForProperty("menuItems"))),
                        fieldWithPath("menuItems[].id").description("Dish id").optional()
                                .attributes(key("constraints").value(constraintDescMenuItem.descriptionsForProperty("id"))),
                        fieldWithPath("menuItems[].name").description("Dish name").optional()
                                .attributes(key("constraints").value(constraintDescMenuItem.descriptionsForProperty("name"))),
                        fieldWithPath("menuItems[].price").description("Dish price").optional()
                                .attributes(key("constraints").value(constraintDescMenuItem.descriptionsForProperty("price")))
                ));
    }

    private RestDocumentationResultHandler getRequestParamDocForOneMenuTo() {

        ConstraintDescriptions constraintDescMenuTo = new ConstraintDescriptions(MenuTo.class);
        ConstraintDescriptions constraintDescMenuItemTo = new ConstraintDescriptions(MenuItemTo.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("id").description("Menu id")
                                .attributes(key("constraints").value(constraintDescMenuTo.descriptionsForProperty("name"))),
                        fieldWithPath("name").description("Menu name")
                                .attributes(key("constraints").value(constraintDescMenuTo.descriptionsForProperty("name"))),
                        fieldWithPath("menuDate").description("Menu date")
                                .attributes(key("constraints").value(constraintDescMenuTo.descriptionsForProperty("menuDate"))),
                        fieldWithPath("enabled").description("Menu activity marker")
                                .attributes(key("constraints").value(constraintDescMenuTo.descriptionsForProperty("enabled"))),
                        subsectionWithPath("menuItemTos").description("Menu dishes")
                                .attributes(key("constraints").value(constraintDescMenuTo.descriptionsForProperty("menuItemTos"))),
                        fieldWithPath("menuItemTos[].id").description("Dish id").optional()
                                .attributes(key("constraints").value(constraintDescMenuItemTo.descriptionsForProperty("id"))),
                        fieldWithPath("menuItemTos[].name").description("Dish name").optional()
                                .attributes(key("constraints").value(constraintDescMenuItemTo.descriptionsForProperty("name"))),
                        fieldWithPath("menuItemTos[].price").description("Dish price").optional()
                                .attributes(key("constraints").value(constraintDescMenuItemTo.descriptionsForProperty("price")))
                ));
    }


    private RestDocumentationResultHandler getResponseParamDocForManyMenu() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("[]").description("Menus"),
                        fieldWithPath("[].id").description("Menu id"),
                        fieldWithPath("[].name").description("Menu name"),
                        fieldWithPath("[].menuDate").description("Menu date"),
                        fieldWithPath("[].enabled").description("Menu activity marker"),
                        subsectionWithPath("[].menuItems").description("Menu dishes"),
                        fieldWithPath("[].menuItems[].id").description("Dish id"),
                        fieldWithPath("[].menuItems[].name").description("Dish name"),
                        fieldWithPath("[].menuItems[].price").description("Dish price")
                ));
    }

    private RestDocumentationResultHandler getErrorResponseParamDoc() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("url").description("Request url"),
                        fieldWithPath("type").description("Error type"),
                        fieldWithPath("typeMessage").description("Error type message"),
                        fieldWithPath("details").description("Error details")
                ));
    }
}
