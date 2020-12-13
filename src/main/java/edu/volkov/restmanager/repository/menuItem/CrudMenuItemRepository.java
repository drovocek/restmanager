package edu.volkov.restmanager.repository.menuItem;

import edu.volkov.restmanager.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuItemRepository extends JpaRepository<MenuItem, Integer> {
    //ADMIN
    @Modifying
    @Transactional
    @Query("DELETE FROM MenuItem mi WHERE mi.id=:id AND mi.menu.id=:menuId")
    int delete(int id, int menuId);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.menu.id=:menuId")
    List<MenuItem> getAllByMenuId(int menuId);
}
