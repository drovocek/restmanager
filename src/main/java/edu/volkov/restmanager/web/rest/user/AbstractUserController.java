package edu.volkov.restmanager.web.rest.user;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.service.UserService;
import edu.volkov.restmanager.to.UserTo;
import edu.volkov.restmanager.util.model.UserUtil;
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
        log.info("\n getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("\n get {}", id);
        return service.get(id);
    }

    public User create(UserTo userTo) {
        log.info("\n create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("\n create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("\n delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("\n update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("\n update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }

    public User getByMail(String email) {
        log.info("\n getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "\n enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }
}