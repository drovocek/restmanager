package edu.volkov.restmanager.web.user;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.assureIdConsistent;
import static edu.volkov.restmanager.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    public List<User> getAll() {
        log.info("\n getAll users");
        return service.getAll();
    }

    public User get(int id) {
        log.info("\n get user:{}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("\n create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("\n delete user:{}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("\n update {} with id:{}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("\nuser getByEmail {}", email);
        return service.getByEmail(email);
    }
}