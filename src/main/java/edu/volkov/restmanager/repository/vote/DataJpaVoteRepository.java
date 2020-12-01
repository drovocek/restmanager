package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.repository.user.CrudUserRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class DataJpaVoteRepository implements VoteRepository {

    private final CrudVoteRepository crudVoteRepo;
    private final CrudUserRepository crudUserRepo;
    private final CrudRestaurantRepository crudRestRepo;

    public DataJpaVoteRepository(
            CrudVoteRepository crudVoteRepo,
            CrudUserRepository crudUserRepo,
            CrudRestaurantRepository crudRestRepo
    ) {
        this.crudVoteRepo = crudVoteRepo;
        this.crudUserRepo = crudUserRepo;
        this.crudRestRepo = crudRestRepo;
    }

    @Override
    public Vote save(Integer voteId, Integer userId, Integer restId, LocalDate voteDate) {
        if (voteId != null && get(userId, voteDate) == null) {
            return null;
        }
        Vote vote = construct(voteId, userId, restId, voteDate);
        return crudVoteRepo.save(vote);
    }

    @Override
    public boolean delete(int voteId) {
        return crudVoteRepo.delete(voteId) != 0;
    }

    @Override
    public Vote get(Integer userId, LocalDate voteDate) {
        return crudVoteRepo.findByUserIdAndVoteDate(userId, voteDate)
                .filter(vote -> vote.getUser().getId().equals(userId))
                .orElse(null);
    }

    private Vote construct(Integer voteId, Integer userId, Integer restaurantId, LocalDate voteDate) {
        return new Vote(
                voteId,
                crudUserRepo.getOne(userId),
                crudRestRepo.getOne(restaurantId),
                voteDate
        );
    }
}
