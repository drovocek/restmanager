package edu.volkov.restmanager;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static edu.volkov.restmanager.RestaurantTestData.REST1_ID;
import static edu.volkov.restmanager.RestaurantTestData.rest1;
import static edu.volkov.restmanager.UserTestData.USER_ID;
import static edu.volkov.restmanager.UserTestData.user;
import static org.junit.Assert.assertEquals;

public class VoteTestData {
    public final static int VOTE1_ID = 0;
    public final static int VOTE_NOT_FOUND_ID = 10;
    public final static LocalDate TODAY = LocalDate.of(2020, 1, 27);
    public final static LocalDate TOMORROW = TODAY.plus(1, ChronoUnit.DAYS);

    public static final Vote vote1 = new Vote(VOTE1_ID, user, rest1, LocalDate.of(2020, 1, 27));
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, user, rest1, LocalDate.of(2020, 1, 28));
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, user, rest1, LocalDate.of(2020, 1, 29));
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, user, rest1, LocalDate.of(2020, 1, 30));
    public static final Vote vote5 = new Vote(VOTE1_ID + 4, user, rest1, LocalDate.of(2020, 2, 27));
    public static final Vote vote6 = new Vote(VOTE1_ID + 5, user, rest1, LocalDate.of(2020, 3, 28));
    public static final Vote vote7 = new Vote(VOTE1_ID + 6, user, rest1, LocalDate.of(2020, 3, 29));
    public static final Vote vote8 = new Vote(VOTE1_ID + 7, user, rest1, LocalDate.of(2020, 3, 30));
}
