package edu.volkov.restmanager.web.user;

import edu.volkov.restmanager.model.User;
import org.springframework.stereotype.Controller;

import static edu.volkov.restmanager.web.SecurityUtil.authUserId;

@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }
}