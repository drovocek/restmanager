package edu.volkov.restmanager.web.rest.user;

import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.service.UserService;
import edu.volkov.restmanager.util.exception.ErrorType;
import edu.volkov.restmanager.util.exception.NotFoundException;
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

public class AdminControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminController.REST_URL + '/';

    @Autowired
    private UserService service;

    @Test
    public void getGood() throws Exception {
        perform(get(REST_URL + "{id}", ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin))
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("User id")
                        )
                ))
                .andDo(getResponseParamDocForOneUser());
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + 10)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(getErrorResponseParamDoc());
    }

    @Test
    public void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + admin.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin))
                .andDo(document("{class-name}/{method-name}",
                        requestParameters(
                                parameterWithName("email").description("User email"))
                        )
                )
                .andDo(getResponseParamDocForOneUser());
    }

    @Test
    public void deleteGood() throws Exception {
        perform(delete(REST_URL + "{id}", USER1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}", pathParameters(
                        parameterWithName("id").description("User id")
                )));

        assertThrows(NotFoundException.class, () -> service.get(USER1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(delete(REST_URL + "{id}", USER_NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("User id")
                        )
                ))
                .andDo(getErrorResponseParamDoc());
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
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
    public void update() throws Exception {
        User updated = getUpdated();

        perform(put(REST_URL + "{id}", USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("User id")
                        )
                ))
                .andDo(getRequestParamDocForOneUpdatedUser());

        USER_MATCHER.assertMatch(service.get(USER1_ID), updated);
    }

    @Test
    public void createWithLocation() throws Exception {
        User newUser = getNew();

        ResultActions action = perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated())
                .andDo(getRequestParamDocForOneNewUser())
                .andDo(getResponseParamDocForOneUser());

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
                .andExpect(USER_MATCHER.contentJson(admin, user1, user2))
                .andDo(getResponseParamDocForManyUsers());
    }

    @Test
    public void enable() throws Exception {
        perform(patch(REST_URL + "{id}", USER1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("User id"))
                        )
                )
                .andDo(document("{class-name}/{method-name}",
                        requestParameters(
                                parameterWithName("enabled").description("User activity marker"))
                ));


        assertFalse(service.get(USER1_ID).isEnabled());
    }

    @Test
    public void enableNotFound() throws Exception {
        perform(patch(REST_URL + "{id}", USER_NOT_FOUND)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("id").description("User id")
                        )
                ))
                .andDo(getErrorResponseParamDoc()).andDo(document("{class-name}/{method-name}",
                requestParameters(
                        parameterWithName("enabled").description("User activity marker"))
        ));
    }

    @Test
    public void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(invalid, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    public void updateInvalid() throws Exception {
        User invalid = new User(user1);
        invalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(invalid, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    public void updateHtmlUnsafe() throws Exception {
        User updated = new User(user1);
        updated.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        User updated = new User(user1);
        updated.setEmail("admin@gmail.com");

        perform(MockMvcRequestBuilders.put(REST_URL + USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void createDuplicate() throws Exception {
        User expected = new User(null, "New", "user1@yandex.ru", "newPass", Role.USER, Role.ADMIN);

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
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

    private RestDocumentationResultHandler getRequestParamDocForOneUpdatedUser() {

        ConstraintDescriptions constraintDescUserTo = new ConstraintDescriptions(User.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("id").description("User id")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("name"))),
                        fieldWithPath("name").description("User name")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("name"))),
                        fieldWithPath("email").description("User email")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("email"))),
                        fieldWithPath("registered").description("User registration date/time")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("registered"))),
                        fieldWithPath("enabled").description("User activity marker")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("enabled"))),
                        fieldWithPath("roles.[]").description("User roles")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("roles")))
                ));
    }

    private RestDocumentationResultHandler getRequestParamDocForOneNewUser() {

        ConstraintDescriptions constraintDescUserTo = new ConstraintDescriptions(User.class);

        return document("{class-name}/{method-name}",
                requestFields(
                        fieldWithPath("name").description("User name")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("name"))),
                        fieldWithPath("email").description("User email")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("email"))),
                        fieldWithPath("registered").description("User registration date/time")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("registered"))),
                        fieldWithPath("password").description("User registration date/time")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("password"))),
                        fieldWithPath("enabled").description("User activity marker")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("enabled"))),
                        fieldWithPath("roles.[]").description("User roles")
                                .attributes(key("constraints").value(constraintDescUserTo.descriptionsForProperty("roles")))
                ));
    }

    private RestDocumentationResultHandler getResponseParamDocForManyUsers() {
        return document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("[]").description("Users"),
                        fieldWithPath("[].id").description("User id"),
                        fieldWithPath("[].name").description("User name"),
                        fieldWithPath("[].email").description("User email"),
                        fieldWithPath("[].registered").description("User registration date/time"),
                        fieldWithPath("[].enabled").description("User activity marker"),
                        fieldWithPath("[].roles.[]").description("User roles")
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