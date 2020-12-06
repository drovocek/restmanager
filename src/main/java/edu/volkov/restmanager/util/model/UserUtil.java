package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.to.UserTo;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {
//    public static UserTo createTo(User user) {
//        return new UserTo(
//                user.id(),
//                user.getName(),
//                user.getEmail(),
//                user.getPassword(),
//                user.getRegistered(),
//                user.isEnabled(),
//                user.getRoles()
//        );
//    }
//
//    public static List<UserTo> getTos(List<User> users) {
//        return users.stream()
//                .map(UserUtil::createTo)
//                .collect(Collectors.toList());
//    }


    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}




