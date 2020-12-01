package edu.volkov.restmanager;

import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import static edu.volkov.restmanager.testdata.RestaurantTestData.rest1;
import static edu.volkov.restmanager.testdata.RestaurantTestData.rest2;
import static edu.volkov.restmanager.testdata.UserTestData.user1;
import static edu.volkov.restmanager.testdata.UserTestData.user2;

public class VoteTestData {
    public final static int VOTE1_ID = 0;
    public final static LocalDate TODAY = LocalDate.of(2020, 1, 27);

    public static final Vote vote1 = new Vote(VOTE1_ID, user1, rest1, TODAY);
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, user1, rest1, TODAY.plus(Period.ofDays(1)));
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, user1, rest1, TODAY.plus(Period.ofDays(2)));
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, user1, rest2, TODAY.plus(Period.ofDays(3)));
    public static final Vote vote5 = new Vote(VOTE1_ID + 4, user1, rest2, TODAY.plus(Period.ofDays(4)));
    public static final Vote vote6 = new Vote(VOTE1_ID + 5, user2, rest1, TODAY);
    public static final Vote vote7 = new Vote(VOTE1_ID + 6, user2, rest2, TODAY.plus(Period.ofDays(1)));
    public static final Vote vote8 = new Vote(VOTE1_ID + 7, user2, rest2, TODAY.plus(Period.ofDays(2)));

    public static final List<Vote> user1Votes = Arrays.asList(vote1, vote2, vote3, vote4, vote5);
    public static final List<Vote> user2Votes = Arrays.asList(vote6, vote7, vote8);
}
