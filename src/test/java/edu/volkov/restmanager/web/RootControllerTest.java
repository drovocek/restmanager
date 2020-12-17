package edu.volkov.restmanager.web;

import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.testdata.MenuTestData;
import edu.volkov.restmanager.web.rest.restaurant.UserRestaurantController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static edu.volkov.restmanager.TestUtil.userAuth;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST_WITH_MENU_MATCHER;
import static edu.volkov.restmanager.testdata.RestaurantTestData.rest1WithDayEnabledMenusAndItems;
import static edu.volkov.restmanager.testdata.UserTestData.*;
import static edu.volkov.restmanager.testdata.UserTestData.user2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RootControllerTest extends AbstractControllerTest {

    @Autowired
    protected RestaurantService service;

    @Test
    public void getUsers() throws Exception {
        perform(get("/users")
                .with(userAuth(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/rest/admin/users"));
    }

    @Test
    public void unAuth() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/login"));
    }
}