package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Vote;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static edu.volkov.restmanager.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.UserTestData.USER_ID;
import static edu.volkov.restmanager.service.VoteService.setTimeLimit;
import static org.junit.Assert.*;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    protected VoteService service;

    @Test
    public void vote() {
        service.vote(USER_ID, REST1_ID);
        assertNotNull(service.getByUserIdAndVoteDate(USER_ID, LocalDate.now()));
    }

    @Test
    public void voteDoubleForOneRestaurantBeforeTimeLimit() {
        setTimeLimit(LocalTime.MAX);
        service.vote(USER_ID, REST1_ID);
        service.vote(USER_ID, REST1_ID);
        assertNull(service.getByUserIdAndVoteDate(USER_ID, LocalDate.now()));
    }

    @Test
    public void voteDoubleForOneRestaurantAfterTimeLimit() {
        setTimeLimit(LocalTime.MIN);
        service.vote(USER_ID, REST1_ID);
        service.vote(USER_ID, REST1_ID);
        assertNotNull(service.getByUserIdAndVoteDate(USER_ID, LocalDate.now()));
    }

    @Test
    public void voteDoubleForDifferentRestaurantsBeforeTimeLimit() {
        setTimeLimit(LocalTime.MAX);
        service.vote(USER_ID, REST1_ID);
        service.vote(USER_ID, REST1_ID + 1);
        Vote vote = service.getByUserIdAndVoteDate(USER_ID, LocalDate.now());
        assertEquals((long) vote.getRestaurant().getId(), REST1_ID + 1);
    }

    @Test
    public void voteDoubleForDifferentRestaurantsAfterTimeLimit() {
        setTimeLimit(LocalTime.MIN);
        service.vote(USER_ID, REST1_ID);
        service.vote(USER_ID, REST1_ID + 1);
        Vote vote = service.getByUserIdAndVoteDate(USER_ID, LocalDate.now());
        assertEquals((long) vote.getRestaurant().getId(), REST1_ID);
    }
}
