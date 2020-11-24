package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") Integer id);

    @Query("SELECT r FROM Restaurant r WHERE r.name=?1")
    Restaurant getByName(@Param("name") String name);

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menus m WHERE m.menuDate=:date")
    List<Restaurant> getAllWithDayMenu(@Param("date") LocalDate date, Sort sorter);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Restaurant r SET r.votes_quantity=r.votes_quantity-1 WHERE r.id=:restaurantId", nativeQuery = true)
    boolean decrementVoteQuantity(Integer restaurantId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Restaurant r SET r.votes_quantity=r.votes_quantity+1 WHERE r.id=:restaurantId", nativeQuery = true)
    void incrementVoteQuantity(Integer restaurantId);
}
