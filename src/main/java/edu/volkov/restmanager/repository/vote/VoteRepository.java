package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote createAndSaveNewVote(Integer userId, Integer restaurantId, LocalDate voteDate);

    boolean delete(int id);

    Vote get(int userId, LocalDate voteDate);

    List<Vote> getAll();
}
