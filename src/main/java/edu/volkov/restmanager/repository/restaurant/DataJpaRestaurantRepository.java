package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestRepo;
    private final CrudMenuRepository crudMenuRepo;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRestRepo, CrudMenuRepository crudMenuRepo) {
        this.crudRestRepo = crudRestRepo;
        this.crudMenuRepo = crudMenuRepo;
    }

    //USER
    @Transactional
    public Restaurant getWithDayEnabledMenu(Integer id) {
        Restaurant restaurant = crudRestRepo.findById(id).orElse(null);
        if (restaurant == null) {
            return null;
        } else {
            //TODO now()
            LocalDate startDate = LocalDate.of(2020, 1, 27);
            LocalDate endDate = startDate;
            List<Menu> dayMenu = crudMenuRepo.getByRestIdBetweenDates(id, startDate, endDate, SORT_NAME);
            List<Menu> dayEnabledMenu = dayMenu.stream().filter(Menu::isEnabled).collect(Collectors.toList());

            restaurant.setMenus(dayEnabledMenu);
            return restaurant;
        }
    }

    public List<Restaurant> getAllWithoutMenu() {
        return crudRestRepo.findAll(SORT_NAME);
    }

    //ADMIN
    @Override
    public Restaurant save(Restaurant restaurant) {
        return crudRestRepo.save(restaurant);
    }

    @Override
    public boolean delete(Integer id) {
        return crudRestRepo.delete(id) != 0;
    }

    @Override
    public Restaurant get(Integer id) {
        return crudRestRepo.findById(id).orElse(null);
    }
}
