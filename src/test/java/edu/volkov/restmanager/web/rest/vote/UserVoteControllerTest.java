package edu.volkov.restmanager.web.rest.vote;

import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.web.AbstractControllerTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static edu.volkov.restmanager.TestUtil.userHttpBasic;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteController.REST_URL + '/';

    @Autowired
    protected VoteService voteService;

    @Test
    public void vote() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + REST1_ID)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assert.assertNotNull(voteService.get(USER1_ID, LocalDate.now()));
    }

    @Test
    public void voteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID))
                .andExpect(status().isUnauthorized());
    }
}