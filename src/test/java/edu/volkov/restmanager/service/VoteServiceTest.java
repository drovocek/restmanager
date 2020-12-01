package edu.volkov.restmanager.service;

import edu.volkov.restmanager.AbstractTest;
import edu.volkov.restmanager.model.Vote;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static edu.volkov.restmanager.service.VoteService.setTimeLimit;
import static edu.volkov.restmanager.testdata.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.testdata.UserTestData.USER1_ID;
import static org.junit.Assert.*;

public class VoteServiceTest extends AbstractTest {

    @Autowired
    protected VoteService service;

    @Test
    public void vote() {
        service.vote(USER1_ID, REST1_ID);
        assertNotNull(service.get(USER1_ID, LocalDate.now()));
    }

    @Test
    public void voteDoubleForOneRestaurantBeforeTimeLimit() {
        setTimeLimit(LocalTime.MAX);
        service.vote(USER1_ID, REST1_ID);
        service.vote(USER1_ID, REST1_ID);
        assertNull(service.get(USER1_ID, LocalDate.now()));
    }

    @Test
    public void voteDoubleForOneRestaurantAfterTimeLimit() {
        setTimeLimit(LocalTime.MIN);
        service.vote(USER1_ID, REST1_ID);
        service.vote(USER1_ID, REST1_ID);
        assertNotNull(service.get(USER1_ID, LocalDate.now()));
    }

    @Test
    public void voteDoubleForDifferentRestaurantsBeforeTimeLimit() {
        setTimeLimit(LocalTime.MAX);
        service.vote(USER1_ID, REST1_ID);
        service.vote(USER1_ID, REST1_ID + 1);
        Vote vote = service.get(USER1_ID, LocalDate.now());
        assertEquals((long) vote.getRestaurant().getId(), REST1_ID + 1);
    }

    @Test
    public void voteDoubleForDifferentRestaurantsAfterTimeLimit() {
        setTimeLimit(LocalTime.MIN);
        service.vote(USER1_ID, REST1_ID);
        service.vote(USER1_ID, REST1_ID + 1);
        Vote vote = service.get(USER1_ID, LocalDate.now());
        assertEquals((long) vote.getRestaurant().getId(), REST1_ID);
    }
}
