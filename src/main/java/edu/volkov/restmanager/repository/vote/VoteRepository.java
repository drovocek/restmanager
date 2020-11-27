package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote constructVote(Integer voteId, Integer userId, Integer restaurantId, LocalDate voteDate);

    boolean delete(int id);

    Vote getByUserIdAndVoteDate(int userId, LocalDate voteDate);

    Vote save(Vote vote);
}
