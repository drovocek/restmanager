package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMenuRepository implements MenuRepository {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.ASC, "id");

    private final CrudMenuRepository crudMenuRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaMenuRepository(CrudMenuRepository crudRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudMenuRepository = crudRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    //USER
    @Override
    public List<Menu> getBetween(LocalDate startDate, LocalDate endDate) {
        return crudMenuRepository.getBetween(startDate, endDate, SORT_DATE);
    }

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
    public boolean delete(Integer id) {
        return crudMenuRepository.delete(id) != 0;
    }

    @Override
    public Menu get(Integer id) {
        return crudMenuRepository.findById(id).orElse(null);
    }

    @Override
    public List<Menu> getAll() {
        return crudMenuRepository.findAll(SORT_DATE);
    }

    @Override
    public List<Menu> getAllByName(String name) {
        return crudMenuRepository.findAllByName(name);
    }
}
