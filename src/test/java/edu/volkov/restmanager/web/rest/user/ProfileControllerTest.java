package edu.volkov.restmanager.web.rest.user;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.service.UserService;
import edu.volkov.restmanager.to.UserTo;
import edu.volkov.restmanager.util.exception.ErrorType;
import edu.volkov.restmanager.util.model.UserUtil;
import edu.volkov.restmanager.web.AbstractControllerTest;
import edu.volkov.restmanager.web.ExceptionInfoHandler;
import edu.volkov.restmanager.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static edu.volkov.restmanager.TestUtil.readFromJson;
import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.UserTestData.*;
import static edu.volkov.restmanager.web.rest.user.ProfileController.REST_URL;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void getGood() throws Exception {
        perform(get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1))
                .andDo(getResponseParamDocForOneUser());
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(document("{class-name}/{method-name}"));
    }

    @Test
    public void deleteGood() throws Exception {
        perform(delete(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}"));

        USER_MATCHER.assertMatch(userService.getAll(), admin, user2);
    }

    @Test
    public void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);

        ResultActions action = perform(post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(getResponseParamDocForOneUser())
                .andDo(getRequestParamDocForOneUser());

        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    public void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    public void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user1))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(getRequestParamDocForOneUser());

        USER_MATCHER.assertMatch(userService.get(USER1_ID), UserUtil.updateFromTo(new User(user1), updatedTo));
    }

    @Test
    public void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user1))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "admin@gmail.com", "newPassword");

        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user1))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR))
                .andExpect(detailMessage(ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL));
    }

    private RestDocumentationResultHandler getResponseParamDocForOneUser() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("id").description("User id"),
                        fieldWithPath("name").description("User name"),
                        fieldWithPath("email").description("User email"),
                        fieldWithPath("registered").description("User registration date/time"),
                        fieldWithPath("enabled").description("User activity marker"),
                        fieldWithPath("roles.[]").description("User roles")
                ));
    }

    private RestDocumentationResultHandler getRequestParamDocForOneUser() {

        ConstraintDescriptions constraintDescUserTo = new ConstraintDescriptions(UserTo.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("name").description("User name")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("name"))),
                        fieldWithPath("email").description("User email")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("email"))),
                        fieldWithPath("password").description("User password")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("password")))
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