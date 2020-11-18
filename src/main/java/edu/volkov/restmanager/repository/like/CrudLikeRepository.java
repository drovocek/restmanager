package edu.volkov.restmanager.repository.like;

import edu.volkov.restmanager.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface CrudLikeRepository extends JpaRepository<Like, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Like l WHERE l.user.id=:id AND l.likeDate=:likeDate")
    int delete(@Param("id") Integer id, @Param("likeDate") LocalDate likeDate);
}
