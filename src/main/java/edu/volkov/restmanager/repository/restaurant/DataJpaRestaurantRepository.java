package edu.volkov.restmanager.repository.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRestaurantRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    //USER
    @Override
    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Restaurant> getBetweenWithEnabledMenu(LocalDate startDate, LocalDate endDate) {
        return crudRestaurantRepository.getBetweenWithEnabledMenu(startDate, endDate);
    }

    //ADMIN
    @Override
    public Restaurant save(Restaurant restaurant) {
        return crudRestaurantRepository.save(restaurant);
    }

    @Override
    public boolean delete(Integer id) {
        return crudRestaurantRepository.delete(id) != 0;
    }

    @Override
    public Restaurant get(Integer id) {
        return crudRestaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getByName(String name) {
        return crudRestaurantRepository.getByName(name);
    }

    @Override
    public List<Restaurant> getAllWithoutMenu() {
        return crudRestaurantRepository.findAll(SORT_NAME);
    }


    //COMMON
//    @Override
//    public List<Restaurant> getAllWithMenuBetween(LocalDate startDate, LocalDate endDate) {
//        return crudRestaurantRepository.findAllByNameAndAddressRegex(startDate, endDate, SORT_NAME);
//    }
}
