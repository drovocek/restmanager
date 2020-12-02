package edu.volkov.restmanager.to;

import edu.volkov.restmanager.model.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserTo {

    private final Integer id;

    private final String name;

    private final String email;

    private final String password;

    private final LocalDateTime registered;

    private final boolean enabled;

    private final Set<Role> roles;
}
