package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.vote.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final VoteRepository repository;
    private LocalTime changeTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    //USER
    public Vote get(int userId, LocalDate voteDate) {
        return repository.get(userId, voteDate);
    }

    public void vote(int userId, int restaurantId) {
        LocalDate votingDate = LocalDate.now();
        boolean beforeTimeLimit = LocalTime.now().isBefore(changeTimeLimit);
        Vote lastUserVoteToDate = get(userId, votingDate);

        if (lastUserVoteToDate == null) {
            constructAndCreate(userId, restaurantId, votingDate);
        } else if (lastUserVoteToDate.getRestaurant().getId() == restaurantId) {
            constructAndUpdate(lastUserVoteToDate.getId(), userId, restaurantId, votingDate);
        } else if (beforeTimeLimit) {
            delete(lastUserVoteToDate.getId());
        }
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
