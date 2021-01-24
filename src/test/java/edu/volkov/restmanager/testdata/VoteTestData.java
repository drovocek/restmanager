package edu.volkov.restmanager.testdata;

import edu.volkov.restmanager.TestMatcher;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.to.VoteTo;

public class VoteTestData {

    public static final TestMatcher<VoteTo> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(VoteTo.class,"restaurant","user");

    public static final int VOTE1_ID = 0;
    public static final int VOTE_NOT_FOUND = 10;

    public static final Vote vote1 = new Vote(VOTE1_ID, UserTestData.user1, RestaurantTestData.rest1, MenuTestData.TODAY);
}
