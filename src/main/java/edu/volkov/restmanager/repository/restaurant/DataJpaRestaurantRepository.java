package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    private static final LocalDate TEST_TODAY = LocalDate.of(2020, 1, 27);

    private final CrudRestaurantRepository crudRestRepo;
    private final CrudMenuRepository crudMenuRepo;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRestRepo, CrudMenuRepository crudMenuRepo) {
        this.crudRestRepo = crudRestRepo;
        this.crudMenuRepo = crudMenuRepo;
    }

    //USER
    @Override
    @Transactional
    public Restaurant getWithDayEnabledMenu(int id) {
        Restaurant restaurant = crudRestRepo.findById(id).orElse(null);
        if (restaurant == null || !restaurant.isEnabled()) {
            return null;
        } else {
            //TODO now()
            List<Menu> dayMenu = crudMenuRepo.getBetween(TEST_TODAY, TEST_TODAY, id);
            List<Menu> dayEnabledMenu = dayMenu.stream().filter(Menu::isEnabled).collect(Collectors.toList());

            restaurant.setMenus(dayEnabledMenu);
            return restaurant;
        }
    }

    @Override
    @Transactional
    public List<Restaurant> getAllWithDayEnabledMenu() {
        List<Restaurant> all= crudRestRepo.findAll(SORT_NAME);

        if (all.isEmpty()) {
            return all;
        } else {
            //TODO now()
            List<Menu> dayEnabledMenus = crudMenuRepo.getAllBetween(TEST_TODAY, TEST_TODAY).stream()
                    .filter(Menu::isEnabled)
                    .collect(Collectors.toList());

            Map<Integer, List<Menu>> menusByRestId = dayEnabledMenus.stream()
                    .filter(Menu::isEnabled)
                    .collect(Collectors.groupingBy(menu -> menu.getRestaurant().getId()));

            all.forEach(rest ->
                    rest.setMenus(Optional.ofNullable(menusByRestId.get(rest.getId()))
                            .orElse(Collections.emptyList())
                    )
            );
            return all;
        }
    }

    //ADMIN
    @Override
    public Restaurant save(Restaurant restaurant) {
        return crudRestRepo.save(restaurant);
    }

    @Override
    public boolean delete(int id) {
        return crudRestRepo.delete(id) != 0;
    }

    @Override
    public Restaurant get(int id) {
        return crudRestRepo.findById(id).orElse(null);
    }
}
