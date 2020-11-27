package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.FilterJoinTables;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@FilterJoinTables(
        @FilterJoinTable(name = "betweenDates", condition = ":menu_date <= startDate and :maxLength >= endDate")
)
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

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @OrderBy("menuDate DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Menu> menus;

    @Formula("(SELECT COUNT(*) FROM Vote v WHERE v.restaurant_id = id)")
    private int votesQuantity;

    public Restaurant(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Restaurant(Integer id, String name, String address, String phone, boolean enabled, int votesQuantity) {
        super(id, name);
        this.address = address;
        this.phone = phone;
        this.enabled = enabled;
        this.votesQuantity = votesQuantity;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.isEnabled(), restaurant.getVotesQuantity());
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", votesQuantity=" + votesQuantity +
                '}';
    }
}
