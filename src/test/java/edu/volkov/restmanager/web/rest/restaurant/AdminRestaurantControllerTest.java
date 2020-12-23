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
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    protected RestaurantService service;

    @Test
    public void create() throws Exception {
        Restaurant newRest = RestaurantTestData.getNew();

        ResultActions action = perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newRest)))
                .andExpect(status().isCreated())
                .andDo(getRequestParamDocForOneRest())
                .andDo(getResponseParamDocForOneRest());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRest.setId(newId);

        REST_MATCHER.assertMatch(created, newRest);
        REST_MATCHER.assertMatch(service.getWithoutMenu(newId), newRest);
    }

    @Test
    public void update() throws Exception {
        RestaurantTo updatedTo = asTo(getUpdated());

        perform(put(REST_URL + "{id}", REST1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("Restaurant id")
                        )
                ))
                .andDo(getRequestParamDocForOneRestTo());

        REST_MATCHER.assertMatch(
                service.getWithoutMenu(REST1_ID),
                RestaurantUtil.updateFromTo(new Restaurant(rest1), asTo(getUpdated()))
        );
    }

    @Test
    public void deleteGood() throws Exception {
        perform(delete(REST_URL + "{id}", REST1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("Restaurant id")
                        ))
                );

        assertThrows(NotFoundException.class, () -> service.getWithoutMenu(REST1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(delete(REST_URL + "{id}", REST_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("Restaurant id")
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
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden())
                .andDo(document("{class-name}/{method-name}"));
    }

    @Test
    public void getWithoutMenu() throws Exception {
        perform(get(REST_URL + "{id}", REST1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_MATCHER.contentJson(rest1))
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("Restaurant id")
                        )
                ))
                .andDo(getResponseParamDocForOneRest());
    }

    @Test
    public void getWithoutMenuNotFound() throws Exception {
        perform(get(REST_URL + "{id}", REST_NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("Restaurant id")
                        )
                ))
                .andDo(getErrorResponseParamDoc());
    }

    @Test
    public void getAllWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);

        perform(get(REST_URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        rest1WithDayEnabledMenusAndItems,
                        rest2WithDayEnabledMenusAndItems
                        )
                )
                .andDo(document("{class-name}/{method-name}"))
                .andDo(getResponseParamDocForManyRest());
    }

    @Test
    public void getFilteredWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);

        perform(get(REST_URL + "filter/")
                .param("name", "rest1")
                .param("address", "address1")
                .param("enabled", "")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        Collections.singletonList(rest1WithDayEnabledMenusAndItems)
                ))
                .andDo(document("{class-name}/{method-name}",
                        requestParameters(
                                parameterWithName("name").description("Restaurant name"),
                                parameterWithName("address").description("Restaurant address"),
                                parameterWithName("enabled").description("Restaurant activity marker")
                        )
                        )
                )
                .andDo(getResponseParamDocForManyRest());
    }

    @Test
    public void enable() throws Exception {
        perform(patch(REST_URL + "{id}", REST1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("Restaurant id")
                        )
                ))
                .andDo(document("{class-name}/{method-name}",
                        requestParameters(
                                parameterWithName("enabled").description("Restaurant activity marker"))
                ));

        assertFalse(service.getWithoutMenu(REST1_ID).isEnabled());
    }

    private RestDocumentationResultHandler getResponseParamDocForOneRest() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("id").description("Restaurant id"),
                        fieldWithPath("name").description("Restaurant name"),
                        fieldWithPath("address").description("Restaurant address"),
                        fieldWithPath("phone").description("Restaurant phone"),
                        fieldWithPath("votesQuantity").description("Restaurant votes quantity"),
                        fieldWithPath("enabled").description("Restaurant activity marker"),
                        fieldWithPath("menus").description("Restaurant menus")
                ));
    }

    private RestDocumentationResultHandler getRequestParamDocForOneRest() {

        ConstraintDescriptions constraintDescRest = new ConstraintDescriptions(Restaurant.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("name").description("Restaurant name")
                                .attributes(key("constraints").value(constraintDescRest.descriptionsForProperty("name"))),
                        fieldWithPath("address").description("Restaurant address")
                                .attributes(key("constraints").value(constraintDescRest.descriptionsForProperty("address"))),
                        fieldWithPath("phone").description("Restaurant phone")
                                .attributes(key("constraints").value(constraintDescRest.descriptionsForProperty("phone"))),
                        fieldWithPath("enabled").description("Restaurant activity marker")
                                .attributes(key("constraints").value(constraintDescRest.descriptionsForProperty("enabled"))),
                        fieldWithPath("votesQuantity").description("Restaurant phone").optional()
                                .attributes(key("constraints").value(constraintDescRest.descriptionsForProperty("Restaurant votes quantity"))),
                        fieldWithPath("menus").description("Restaurant phone").optional()
                                .attributes(key("constraints").value(constraintDescRest.descriptionsForProperty("Restaurant menus")))
                ));
    }

    private RestDocumentationResultHandler getRequestParamDocForOneRestTo() {

        ConstraintDescriptions constraintDescRestTo = new ConstraintDescriptions(RestaurantTo.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("id").description("Restaurant id")
                                .attributes(key("constraints").value(constraintDescRestTo.descriptionsForProperty("name"))),
                        fieldWithPath("name").description("Restaurant name")
                                .attributes(key("constraints").value(constraintDescRestTo.descriptionsForProperty("name"))),
                        fieldWithPath("address").description("Restaurant address")
                                .attributes(key("constraints").value(constraintDescRestTo.descriptionsForProperty("address"))),
                        fieldWithPath("phone").description("Restaurant phone")
                                .attributes(key("constraints").value(constraintDescRestTo.descriptionsForProperty("phone"))),
                        fieldWithPath("enabled").description("Restaurant activity marker")
                                .attributes(key("constraints").value(constraintDescRestTo.descriptionsForProperty("enabled")))
                ));
    }

    private RestDocumentationResultHandler getResponseParamDocForManyRest() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("[]").description("Restaurants"),
                        fieldWithPath("[].id").description("Restaurant id"),
                        fieldWithPath("[].name").description("Restaurant name"),
                        fieldWithPath("[].address").description("Restaurant address"),
                        fieldWithPath("[].phone").description("Restaurant phone"),
                        fieldWithPath("[].votesQuantity").description("Restaurant votes quantity"),
                        fieldWithPath("[].enabled").description("Restaurant activity marker"),
                        subsectionWithPath("[].menus").description("Restaurant menus"),
                        fieldWithPath("[].menus[].id").description("Menu id"),
                        fieldWithPath("[].menus[].name").description("Menu name"),
                        fieldWithPath("[].menus[].menuDate").description("Menu date"),
                        fieldWithPath("[].menus[].enabled").description("Menu activity marker"),
                        subsectionWithPath("[].menus[].menuItems").description("Menu dishes"),
                        fieldWithPath("[].menus[].menuItems[].id").description("Dish id"),
                        fieldWithPath("[].menus[].menuItems[].name").description("Dish name"),
                        fieldWithPath("[].menus[].menuItems[].price").description("Dish price")
                ));
    }

    private RestDocumentationResultHandler getErrorResponseParamDoc() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("url").description("Request url"),
                        fieldWithPath("type").description("Error type"),
                        fieldWithPath("detail").description("Error details")
                ));
    }
}