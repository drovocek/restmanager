package edu.volkov.restmanager.repository;

import edu.volkov.restmanager.model.User;

import java.util.Set;

public interface UserRepository {
    User save(User user);

    boolean delete(Integer id);

    User get(Integer id);

    default User getWithAllLikes(Integer id) {
        throw new UnsupportedOperationException("method is not supported");
    }

    User getByEmail(String email);

    Set<User> getAll();
}
