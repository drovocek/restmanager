package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.vote.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository repository;

    private static LocalTime changeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote get(int userId, LocalDate voteDate) {
        return repository.get(userId, voteDate);
    }

    public void vote(int userId, int restId) {
        LocalDate votingDate = LocalDate.now();
        boolean inLimit = LocalTime.now().isBefore(changeLimit);
        Vote lastVote = get(userId, votingDate);

        if (lastVote == null) {
            log.info("\n create vote");
            create(userId, restId, votingDate);
        } else if (lastVote.getRestaurant().getId() != restId && inLimit) {
            log.info("\n update vote {}", lastVote.getId());
            update(lastVote.getId(), userId, restId, votingDate);
        } else if (inLimit) {
            log.info("\n delete vote {}", lastVote.getId());
            delete(lastVote.getId());
        }
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public static void setTimeLimit(LocalTime time) {
        changeLimit = time;
    }

    private void update(Integer voteId, Integer userId, Integer restId, LocalDate voteDate) {
        checkNotFoundWithId(repository.save(voteId, userId, restId, voteDate), voteId);
    }

    private void create(Integer userId, Integer restId, LocalDate voteDate) {
        checkNotFound(repository.save(null, userId, restId, voteDate), "vote don't save");
    }
}
