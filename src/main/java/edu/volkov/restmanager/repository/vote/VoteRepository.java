package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;

public interface VoteRepository {

    Vote constructVote(Integer voteId, Integer userId, Integer restaurantId, LocalDate voteDate);

    boolean delete(int id);

    Vote get(int userId, LocalDate voteDate);

    Vote save(Vote vote);
}
