package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.to.UserTo;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {
    public static UserTo createTo(User user) {
        return new UserTo(
                user.id(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRegistered(),
                user.isEnabled(),
                user.getRoles()
        );
    }

    public static List<UserTo> getTos(List<User> users) {
        return users.stream()
                .map(UserUtil::createTo)
                .collect(Collectors.toList());
    }
}




