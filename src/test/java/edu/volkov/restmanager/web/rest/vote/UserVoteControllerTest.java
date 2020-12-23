package edu.volkov.restmanager.web.rest.vote;

import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.web.AbstractControllerTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.USER1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteController.REST_URL + '/';

    @Autowired
    protected VoteService voteService;

    @Test
    public void vote() throws Exception {
        perform(patch(REST_URL + "{restId}", REST1_ID)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("restId").description("Restaurant id")
                        )
                ))
                .andDo(document("{class-name}/{method-name}"));

        Assert.assertNotNull(voteService.get(USER1_ID, LocalDate.now()));
    }

    @Test
    public void voteUnAuth() throws Exception {
        perform(patch(REST_URL + "{restId}", REST1_ID))
                .andExpect(status().isUnauthorized())
                .andDo(document("{class-name}/{method-name}"));
    }
}