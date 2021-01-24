package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
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

    Restaurant getByName(@Param("name") String name);

    @Query("SELECT r FROM Restaurant r WHERE " +
            "(LOWER(r.name) LIKE LOWER(CONCAT('%',COALESCE(:name,'%'),'%'))) AND " +
            "(LOWER(r.address) LIKE LOWER(CONCAT('%',COALESCE(:address,'%'),'%'))) AND " +
            "(r.enabled=COALESCE(:enabled, true) OR " +
            "r.enabled=COALESCE(:enabled, false)) ")
    List<Restaurant> getFilteredByNameAndAddressAndEnabled(@Param("name") String name,
                                                           @Param("address") String address,
                                                           @Param("enabled") Boolean enabled,
                                                           Sort sort);
}
