package edu.volkov.restmanager.web.rest.vote;

import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.web.AbstractControllerTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.USER1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static edu.volkov.restmanager.testdata.VoteTestData.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteController.REST_URL + '/';

    @Autowired
    protected VoteService service;

    @Test
    public void getGood() throws Exception {

        perform(get(REST_URL + "{id}", VOTE1_ID)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
//                .andDo(document("{class-name}/{method-name}",
//                        pathParameters(
//                                parameterWithName("id").description("User id")
//                        )
//                ))
//                .andDo(getResponseParamDocForOneUser());
    }

    @Test
    public void create() throws Exception {
        perform(post(REST_URL)
                .with(userHttpBasic(user1))
                .param("restId", String.valueOf(REST1_ID)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        requestFields(
                                fieldWithPath("restId").description("Restaurant id")
                        )
                ))
                .andDo(document("{class-name}/{method-name}"));

        Assert.assertNotNull(service.get(USER1_ID, LocalDate.now()));
    }

    @Test
    public void createUnAuth() throws Exception {
        perform(post(REST_URL + "{restId}", REST1_ID))
                .andExpect(status().isUnauthorized())
                .andDo(document("{class-name}/{method-name}"));
    }
}