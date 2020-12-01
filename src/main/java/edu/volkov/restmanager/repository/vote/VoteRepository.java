package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    // null if not found
    boolean delete(int id);

    // null if not found
    Vote get(Integer userId, LocalDate voteDate);

    // null if not found, when updated
    Vote save(Integer voteId, Integer userId, Integer restId, LocalDate voteDate);
}
