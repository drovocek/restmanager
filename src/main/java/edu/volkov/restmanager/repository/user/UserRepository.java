package edu.volkov.restmanager.repository.user;

import edu.volkov.restmanager.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    boolean delete(Integer id);

    User get(Integer id);

    User getByEmail(String email);

    List<User> getAll();
}
