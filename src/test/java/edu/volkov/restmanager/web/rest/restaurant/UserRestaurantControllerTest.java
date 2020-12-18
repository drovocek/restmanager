package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.testdata.MenuTestData;
import edu.volkov.restmanager.web.AbstractControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static edu.volkov.restmanager.testdata.UserTestData.admin;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/';

    @Autowired
    protected RestaurantService service;

    @Test
    public void getWithEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);
        perform(get(REST_URL + "{restId}", REST1_ID)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(rest1WithDayEnabledMenusAndItems))
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id")
                        )
                ))
                .andDo(getResponseParamDocForManyRest());
    }

    @Test
    public void getAllEnabledWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        Collections.singletonList(rest1WithDayEnabledMenusAndItems)
                ));
    }

    @Test
    public void getFilteredRest1EnabledWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter/")
                .param("name", "rest1")
                .param("address", "address1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        Collections.singletonList(rest1WithDayEnabledMenusAndItems)
                ));
    }

    @Test
    public void getFilteredEmptyEnabledWithDayEnabledMenu() throws Exception {
        service.setTestDate(MenuTestData.TODAY);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .with(userHttpBasic(user1))
                .param("name", "rest2")
                .param("address", "address2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk());
    }

    @Test
    public void getAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk());
    }

    private RestDocumentationResultHandler getResponseParamDocForManyRest() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("[]").description("Restaurants"),
                        fieldWithPath("[].id").description("Restaurant id"),
                        fieldWithPath("[].name").description("Restaurant name"),
                        fieldWithPath("[].address").description("Restaurant address"),
                        fieldWithPath("[].phone").description("Restaurant phone"),
                        fieldWithPath("[].votesQuantity").description("Restaurant votes"),
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
}