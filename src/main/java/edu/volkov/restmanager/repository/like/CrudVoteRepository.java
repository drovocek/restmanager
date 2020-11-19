package edu.volkov.restmanager.repository.like;

import edu.volkov.restmanager.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM  Vote l WHERE l.user.id=:userId AND l.voteDate=:voteDate AND l.restaurant.id=:restaurantId")
    int delete(
            @Param("userId") Integer userId,
            @Param("restaurantId") Integer restaurantId,
            @Param("voteDate") LocalDate voteDate);

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO Vote (user_id,restaurant_id,vote_date) VALUES (:userId,:restaurantId,:voteDate)",
            nativeQuery = true)
    void createLike(
            @Param("userId") Integer userId,
            @Param("restaurantId") Integer restaurantId,
            @Param("voteDate") LocalDate voteDate
    );

    @Query("SELECT COUNT(l) FROM Vote l WHERE l.user.id=:userId AND l.voteDate=:voteDate")
    int hasUserVoteToDate(
            @Param("userId") Integer userId,
            @Param("voteDate") LocalDate voteDate
    );
}
