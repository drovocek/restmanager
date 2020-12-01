package edu.volkov.restmanager.repository.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.restaurant.CrudRestaurantRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    private final CrudMenuRepository crudMenuRepo;
    private final CrudRestaurantRepository crudRestRepo;

    public DataJpaMenuRepository(CrudMenuRepository crudMenuRepo, CrudRestaurantRepository crudRestRepo) {
        this.crudMenuRepo = crudMenuRepo;
        this.crudRestRepo = crudRestRepo;
    }

    //TODO for Likes to
    @Transactional
    @Override
    public Menu save(Menu menu, int restId) {
        if (!menu.isNew() && get(menu.getId(), restId) == null) {
            return null;
        }
        menu.setRestaurant(crudRestRepo.getOne(restId));
        return crudMenuRepo.save(menu);
    }

    @Override
    public boolean delete(int id, int restId) {
        return crudMenuRepo.delete(id, restId) != 0;
    }

    @Override
    public Menu get(int id, int restId) {
        return crudMenuRepo.findById(id)
                .filter(menu -> menu.getRestaurant().getId() == restId)
                .orElse(null);
    }

    @Override
    public List<Menu> getAll(int restId) {
        return crudMenuRepo.getAll(restId);
    }

    @Override
    public List<Menu> getBetween(LocalDate startDate, LocalDate endDate, int restId) {
        return crudMenuRepo.getBetween(startDate, endDate, restId);
    }
}
