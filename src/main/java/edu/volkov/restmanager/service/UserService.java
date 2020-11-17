package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        Assert.notNull(user, "user must be not null");
        return repository.save(user);
    }

    public void update(User user) {
        Assert.notNull(user, "user must be not null");
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    public void delete(Integer id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(Integer id) {
        return checkNotFound(repository.get(id), "user by id: " + id + "dos not exist");
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must be not null");
        return checkNotFound(repository.getByEmail(email), "user by email: " + email + "dos not exist");
    }

    public User getWithAllLikes(Integer id) {
        return repository.getWithAllLikes(id);
    }

    public Set<User> getAll() {
        return repository.getAll();
    }
}
