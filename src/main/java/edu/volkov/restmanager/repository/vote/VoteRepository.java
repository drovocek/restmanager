package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote createAndSaveNewVote(Integer userId, Integer restaurantId, LocalDate voteDate);

    boolean delete(Integer userId, LocalDate voteDate);

    Vote get(Integer userId, LocalDate voteDate);

    List<Vote> getAll();
}
