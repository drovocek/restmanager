package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = {"menus","votes"})
@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(name = "restaurants_unique_name_idx", columnNames = "name")})
public class Restaurant extends AbstractNamedEntity {

    @NotBlank
    @Size(min = 5, max = 200)
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank
//    https://www.baeldung.com/java-regex-validate-phone-numbers
    @Pattern(regexp = "^(\\+\\d{1}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
    @Size(max = 20)
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @OrderBy("menuDate DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Menu> menus;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    private List<Menu> votes;

    @Formula("(SELECT COUNT(*) FROM Votes v WHERE v.restaurant_id = id)")
    private Integer votesQuantity;

    public Restaurant(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Restaurant(Integer id, String name, String address, String phone, boolean enabled) {
        super(id, name);
        this.address = address;
        this.phone = phone;
        this.enabled = enabled;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.isEnabled());
    }
}
