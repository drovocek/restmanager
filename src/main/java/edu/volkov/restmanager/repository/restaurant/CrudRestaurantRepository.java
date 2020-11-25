package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(Integer id);

    @Query("SELECT r FROM Restaurant r WHERE r.name=?1")
    Restaurant getByName(String name);

    //USER REPO
    @Query("SELECT r FROM Restaurant r WHERE r.enabled=:enabled")
    List<Restaurant> getFilteredByEnabledWithoutMenu(Boolean enabled, Sort sorter);

    @Query("SELECT r FROM Restaurant r WHERE r.name=:name AND r.address=:address")
    @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Restaurant> getAllByNameAndAddressPart(String name, String address, Sort sorter);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Restaurant r SET r.votes_quantity=r.votes_quantity-1 WHERE r.id=:restaurantId", nativeQuery = true)
    int decrementVoteQuantity(Integer restaurantId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Restaurant r SET r.votes_quantity=r.votes_quantity+1 WHERE r.id=:restaurantId", nativeQuery = true)
    int incrementVoteQuantity(Integer restaurantId);
}
