package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import edu.volkov.restmanager.repository.user.CrudUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.ASC, "vote_date");

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

    @Override
    public Vote createAndSaveNewVote(Integer userId, Integer restaurantId, LocalDate voteDate) {
        Vote vote = createNewVote(userId, restaurantId, voteDate);
        return save(vote);
    }

    @Override
    public boolean delete(Integer userId, LocalDate voteDate) {
        User user = crudUserRepository.getOne(userId);
        return crudVoteRepository.deleteByUserAndVoteDate(user, voteDate) != 0;
    }

    @Override
    public Vote get(Integer userId, LocalDate voteDate) {
        User user = crudUserRepository.getOne(userId);
        return crudVoteRepository.findAllByUserAndVoteDate(user, voteDate)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return crudVoteRepository.findAll(SORT_DATE);
    }

    private Vote save(Vote vote) {
        return crudVoteRepository.save(vote);
    }

    private Vote createNewVote(Integer userId, Integer restaurantId, LocalDate voteDate) {
        return new Vote(
                null,
                crudUserRepository.getOne(userId),
                crudRestaurantRepository.getOne(restaurantId),
                voteDate
        );
    }
}
