package edu.volkov.restmanager.repository.vote;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    int deleteByUserAndVoteDate(
            @Param("user") User user,
            @Param("voteDate") LocalDate voteDate
    );

    Optional<Vote> findAllByUserAndVoteDate(
            @Param("user") User user,
            @Param("voteDate") LocalDate voteDate
    );
}
