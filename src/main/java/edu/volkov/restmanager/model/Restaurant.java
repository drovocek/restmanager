package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = "menus")
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

    @NotNull
    @Column(name = "votes_quantity", nullable = false)
    private Integer votesQuantity = 0;

    @OrderBy("menuDate DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Menu> menus;

    public Restaurant(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Restaurant(Integer id, String name, String address, String phone) {
        super(id, name);
        this.address = address;
        this.phone = phone;
    }

    public Restaurant(Integer id, String name, String address, String phone, boolean enabled, Integer votesQuantity) {
        super(id, name);
        this.address = address;
        this.phone = phone;
        this.enabled = enabled;
        this.votesQuantity = votesQuantity;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.isEnabled(), restaurant.getVotesQuantity());
    }
}
