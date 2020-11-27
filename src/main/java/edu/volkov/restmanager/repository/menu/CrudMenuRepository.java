package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    //USER
    @Query("SELECT m FROM Menu m " +
            "WHERE m.restaurant.id=:restId AND " +
            "m.menuDate>=:startDate AND " +
            "m.menuDate<=:endDate AND " +
            "m.enabled=:enabled")
    List<Menu> getByRestIdBetweenDatesFilteredByEnabled(
            Integer restId,
            LocalDate startDate,
            LocalDate endDate,
            Boolean enabled
    );

    @Query("SELECT m FROM Menu m WHERE m.menuDate>=:startDate AND m.menuDate<=:endDate")
    List<Menu> getAllBetween(LocalDate startDate, LocalDate endDate, Sort sorter);

    //ADMIN
    @Transactional
    @Modifying
    @Query("DELETE FROM  Menu m WHERE m.id=:menuId")
    int delete(Integer menuId);
}
