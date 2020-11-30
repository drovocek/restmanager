package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    //USER
    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m " +
            "WHERE m.restaurant.id=:restId AND " +
            "m.menuDate>=:startDate AND " +
            "m.menuDate<=:endDate")
    List<Menu> getByRestIdBetweenDates(
            Integer restId,
            LocalDate startDate,
            LocalDate endDate,
            Sort sort
    );

    //ADMIN
    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:menuId AND m.restaurant.id=:restaurantId")
    int delete(int menuId, int restaurantId);
}
