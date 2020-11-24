package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM  Menu m WHERE m.id=:menuId")
    int delete(
            @Param("menuId") Integer menuId
    );

    List<Menu> findAllByName(
            @Param("name") String name
    );

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = ?1")
    Menu getWithRestaurant(int menuId);
}
