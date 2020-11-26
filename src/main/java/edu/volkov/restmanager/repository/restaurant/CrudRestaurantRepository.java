package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.FilterJoinTables;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    //USER

    //ADMIN
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(Integer id);

    @Query("SELECT r FROM Restaurant r WHERE r.name=?1")
    Restaurant getByName(String name);

//    @Query(value = "SELECT * FROM Restaurant r " +
////            "JOIN restaurant_menus rm " +
////            "ON r.id = rm.restaurant_id " +
//            "JOIN menu m " +
////            "ON m.id=rm.menus_id " +
//            "ON m.restaurant_id=r.id " +
//            "WHERE m.menu_date=:startDate AND m.menu_date=:endDate",
//            nativeQuery = true)
//    List<Restaurant> getBetweenWithEnabledMenu(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> getBetweenWithEnabledMenu(LocalDate startDate, LocalDate endDate);
}
