package edu.volkov.restmanager.web.rest.user;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.service.UserService;
import edu.volkov.restmanager.testdata.UserTestData;
import edu.volkov.restmanager.util.exception.NotFoundException;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static edu.volkov.restmanager.TestUtil.readFromJson;
import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.UserTestData.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminController.REST_URL + '/';

    @Autowired
    private UserService service;

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + 10)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + admin.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(USER1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + 10)
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
        User updated = UserTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(service.get(USER1_ID), updated);
    }

    @Test
    public void createWithLocation() throws Exception {
        User newUser = UserTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(UserTestData.jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, user1, user2));
    }

    @Test
    public void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(service.get(USER1_ID).isEnabled());
    }
}