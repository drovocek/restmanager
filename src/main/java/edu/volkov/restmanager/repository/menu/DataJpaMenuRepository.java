package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMenuRepository implements MenuRepository {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.DESC, "menuDate");

    private final CrudMenuRepository crudMenuRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaMenuRepository(CrudMenuRepository crudRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudMenuRepository = crudRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    //USER

    //ADMIN
    //TODO for Likes to
    @Override
    public Menu save(Menu menu, int restaurantId) {
        if (!menu.isNew() && get(menu.getId()) == null) {
            return null;
        }

        menu.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudMenuRepository.save(menu);
    }

    @Override
    public boolean delete(int menuId, int restaurantId) {
        return crudMenuRepository.delete(menuId, restaurantId) != 0;
    }

    @Override
    public Menu get(int id) {
        return crudMenuRepository.findById(id).orElse(null);
    }

    @Override
    public List<Menu> getByRestIdBetweenDates(Integer restaurantId, LocalDate startDate, LocalDate endDate) {
        return crudMenuRepository.getByRestIdBetweenDates(restaurantId, startDate, endDate, SORT_DATE);
    }

    @Override
    public List<Menu> getBetween(LocalDate startDate, LocalDate endDate) {
        return crudMenuRepository.getAllBetween(startDate, endDate, SORT_DATE);
    }

    //
    @Override
    public List<Menu> getAll() {
        return crudMenuRepository.findAll(SORT_DATE);
    }
}
