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
@ToString(exclude = "allMenus")
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

    @Formula("(SELECT COUNT(*) FROM Vote l WHERE l.restaurant_id = id)")
    private Integer likeAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Menu> allMenus;

//    @Formula("(SELECT COUNT(*) FROM Menu m WHERE m.menu_date =  CURRENT_DATE())")
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
//    private List<Menu> todayMenus;

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

    public Restaurant(Integer id, String name, String address, String phone, Integer likeAmount) {
        super(id, name);
        this.address = address;
        this.phone = phone;
        this.likeAmount = likeAmount;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.getLikeAmount());
    }
}
