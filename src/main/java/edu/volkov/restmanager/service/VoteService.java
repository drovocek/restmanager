package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.vote.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    //USER
    public Vote get(int userId, LocalDate voteDate) {
        return repository.get(userId, voteDate);
    }

    public Vote constructAndCreate(int userId, int restaurantId, LocalDate votingDate) {
        Assert.isTrue(userId > 0, "userId must be > 0");
        Assert.isTrue(restaurantId > 0, "userId must be > 0");
        Assert.notNull(votingDate, "votingDate must be not null");
        Vote saved = repository.constructVote(null, userId, restaurantId, votingDate);

        return repository.save(saved);
    }

    public void constructAndUpdate(int voteId, int userId, int restaurantId, LocalDate votingDate) {
        Assert.isTrue(userId > 0, "userId must be > 0");
        Assert.isTrue(restaurantId > 0, "userId must be > 0");
        Assert.notNull(votingDate, "votingDate must be not null");
        Vote updated = repository.constructVote(voteId, userId, restaurantId, votingDate);

        checkNotFoundWithId(repository.save(updated), userId);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
