package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.repository.user.CrudUserRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class DataJpaVoteRepository implements VoteRepository {

    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaVoteRepository(
            CrudVoteRepository crudVoteRepository,
            CrudUserRepository crudUserRepository,
            CrudRestaurantRepository crudRestaurantRepository
    ) {
        this.crudVoteRepository = crudVoteRepository;
        this.crudUserRepository = crudUserRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    //USER
    @Override
    public Vote save(Vote vote) {
        if (!vote.isNew() && get(vote.getId(), vote.getVoteDate()) == null) {
            return null;
        }
        return crudVoteRepository.save(vote);
    }

    @Override
    public boolean delete(int voteId) {
        return crudVoteRepository.delete(voteId) != 0;
    }

    @Override
    public Vote get(int userId, LocalDate voteDate) {
        User user = crudUserRepository.getOne(userId);
        return crudVoteRepository.findAllByUserAndVoteDate(user, voteDate)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public Vote constructVote(Integer voteId, Integer userId, Integer restaurantId, LocalDate voteDate) {
        return new Vote(
                voteId,
                crudUserRepository.getOne(userId),
                crudRestaurantRepository.getOne(restaurantId),
                voteDate
        );
    }
}
