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
//    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.restaurant WHERE m.enabled = :enabled AND m.menuDate>=:startDate AND m.menuDate<=:endDate")
//    List<Menu> getFilteredByEnabledBetweenDatesWithRestaurant(Boolean enabled, LocalDate startDate, LocalDate endDate, Sort sorter);

    @Query("SELECT m FROM Menu m WHERE m.menuDate>=:startDate AND m.menuDate<=:endDate")
    List<Menu> getBetween(LocalDate startDate, LocalDate endDate, Sort sorter);

    //ADMIN
    @Transactional
    @Modifying
    @Query("DELETE FROM  Menu m WHERE m.id=:menuId")
    int delete(Integer menuId);

    List<Menu> findAllByName(String name);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = :menuId")
    Menu getWithRestaurant(int menuId);
}
