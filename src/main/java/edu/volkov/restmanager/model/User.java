package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "users_unique_email_idx", columnNames = "email")})
public class User extends AbstractNamedEntity {

    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 5, max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "registered", nullable = false, columnDefinition = "date default now()")
    private LocalDate registered = LocalDate.now();

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            columnNames = {"user_id", "role"},
                            name = "user_roles_unique_idx"
                    )
            }
    )
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    private Set<Role> roles;

    public User(Integer id, String name, String email, String password, boolean enabled, Set<Role> roles, LocalDate registered) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.registered = registered;
    }

    public User(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.isEnabled(), user.getRoles(), user.getRegistered());
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }
}
