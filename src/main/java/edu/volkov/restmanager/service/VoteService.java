package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.CrudRestaurantRepository;
import edu.volkov.restmanager.repository.CrudUserRepository;
import edu.volkov.restmanager.repository.CrudVoteRepository;
import edu.volkov.restmanager.util.exception.NotInTimeLimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@RequiredArgsConstructor
@Service
@Slf4j
public class VoteService {

    private static LocalTime voteTimeLimit = LocalTime.NOON.minus(1, ChronoUnit.HOURS);

    private final CrudVoteRepository voteRepo;
    private final CrudUserRepository userRepo;
    private final CrudRestaurantRepository restRepo;

    public Vote get(int userId, LocalDate voteDate) {
        return voteRepo.findByUserIdAndVoteDate(userId, voteDate)
                .filter(vote -> vote.getUser().getId().equals(userId))
                .orElse(null);
    }

    @Transactional
    public void vote(int userId, int restId) {
        log.info("vote user:{} by restaurant:{}", userId, restId);
        LocalDate voteDate = LocalDate.now();
        boolean inLimit = LocalTime.now().isBefore(voteTimeLimit);
        Vote lastVote = get(userId, voteDate);

        if (lastVote == null) {
            create(construct(userId, restId, voteDate));
        } else if (lastVote.getRestaurant().getId() != restId && inLimit) {
            updateRestId(lastVote, restId);
        } else if (inLimit) {
            delete(lastVote.getId());
        } else {
            throw new NotInTimeLimitException("error.timeLimit");
        }
    }

    public void delete(int id) {
        log.info("delete vote {}", id);
        checkNotFoundWithId(voteRepo.delete(id) != 0, id);
    }

    public static void setVoteTimeLimit(LocalTime time) {
        voteTimeLimit = time;
    }

    private void create(Vote created) {
        log.info("create vote");
        checkNotFound(save(created), "vote don't save");
    }

    private void updateRestId(Vote updated, int restId) {
        log.info("updateRestId vote {}", updated.getId());
        updated.setRestaurant(restRepo.getOne(restId));
    }

    private Vote save(Vote vote) {
        if (!vote.isNew() && get(vote.getUser().getId(), vote.getVoteDate()) == null) {
            return null;
        }

        return voteRepo.save(vote);
    }

    private Vote construct(Integer userId, Integer restaurantId, LocalDate voteDate) {
        return new Vote(null, userRepo.getOne(userId), restRepo.getOne(restaurantId), voteDate);
    }
}
