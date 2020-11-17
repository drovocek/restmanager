package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class DataJpaUserRepository implements UserRepository {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public boolean delete(Integer id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(Integer id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public Set<User> getAll() {
        return new HashSet<>(crudRepository.findAll(SORT_NAME_EMAIL));
    }
}
