package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.vote.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteRepository repository;
    private static LocalTime changeTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    //USER
    public Vote getByUserIdAndVoteDate(int userId, LocalDate voteDate) {
        return repository.getByUserIdAndVoteDate(userId, voteDate);
    }

    public void vote(int userId, int restaurantId) {
        LocalDate votingDate = LocalDate.now();
        boolean beforeTimeLimit = LocalTime.now().isBefore(changeTimeLimit);
        Vote lastUserVoteToDate = getByUserIdAndVoteDate(userId, votingDate);

        if (lastUserVoteToDate == null) {
            log.info("\n create vote");
            constructAndCreate(userId, restaurantId, votingDate);
        } else if (lastUserVoteToDate.getRestaurant().getId() != restaurantId && beforeTimeLimit) {
            log.info("\n update vote {}", lastUserVoteToDate.getId());
            constructAndUpdate(lastUserVoteToDate.getId(), userId, restaurantId, votingDate);
        } else if (beforeTimeLimit) {
            log.info("\n delete vote {}", lastUserVoteToDate.getId());
            delete(lastUserVoteToDate.getId());
        }
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    private Vote constructAndCreate(int userId, int restaurantId, LocalDate votingDate) {
        Assert.isTrue(userId >= 0, "userId must be >= 0");
        Assert.isTrue(restaurantId >= 0, "userId must be >= 0");
        Assert.notNull(votingDate, "votingDate must be not null");
        Vote saved = repository.constructVote(null, userId, restaurantId, votingDate);

        return repository.save(saved);
    }

    private void constructAndUpdate(int voteId, int userId, int restaurantId, LocalDate votingDate) {
        Assert.isTrue(userId >= 0, "userId must be >= 0");
        Assert.isTrue(restaurantId >= 0, "userId must be >= 0");
        Assert.notNull(votingDate, "votingDate must be not null");
        Vote updated = repository.constructVote(voteId, userId, restaurantId, votingDate);

        checkNotFoundWithId(repository.save(updated), userId);
    }

    public static void setTimeLimit(LocalTime time){
        changeTimeLimit=time;
    }
}
