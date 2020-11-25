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
    @Transactional
    @Modifying
    @Query("DELETE FROM  Menu m WHERE m.id=:menuId")
    int delete(Integer menuId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.enabled = :enabled AND m.menuDate>=:startDate AND m.menuDate<=:endDate")
    List<Menu> getFilteredByEnabledBetweenDates(Boolean enabled, LocalDate startDate, LocalDate endDate, Sort sorter);

    List<Menu> findAllByName(String name);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = :menuId")
    Menu getWithRestaurant(int menuId);
}
