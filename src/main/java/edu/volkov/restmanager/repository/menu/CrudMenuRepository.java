package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:menuId AND m.restaurant.id=:restId")
    int delete(int menuId, int restId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restId ORDER BY m.menuDate DESC")
    List<Menu> getAll(int restId);

    @Query("SELECT m FROM Menu m " +
            "WHERE m.restaurant.id=:restId AND " +
            "m.menuDate>=:startDate AND " +
            "m.menuDate<=:endDate" +
            " ORDER BY m.menuDate DESC")
    List<Menu> getBetween(LocalDate startDate, LocalDate endDate, int restId);

    //@EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m " +
            "WHERE m.menuDate>=:startDate AND " +
            "m.menuDate<=:endDate" +
            " ORDER BY m.menuDate DESC")
    List<Menu> getAllBetween(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restId")
    Menu getWithMenuItems(int id, int restId);
}
