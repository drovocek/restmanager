package edu.volkov.restmanager.web.rest.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.web.AbstractControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.RestaurantTestData.*;
import static edu.volkov.restmanager.testdata.UserTestData.admin;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/';

    @Test
    public void getWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(rest1WithDayEnabledMenusAndItems));
    }

    @Test
    public void getAllEnabledWithDayEnabledMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        rest1WithDayEnabledMenusAndItems,
                        rest2WithDayEnabledMenusAndItems
                        )
                );
    }

    @Test
    public void getFilteredEnabledWithDayEnabledMenuAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+ "filter?name=&address="))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        rest1WithDayEnabledMenusAndItems,
                        rest2WithDayEnabledMenusAndItems
                        )
                );
    }

    @Test
    public void getFilteredEnabledWithDayEnabledMenuByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .with(userHttpBasic(user1))
                .param("name", "rest1").param("address", "address1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(REST_WITH_MENU_MATCHER.contentJson(
                        Collections.singletonList(rest1WithDayEnabledMenusAndItems)));
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
}