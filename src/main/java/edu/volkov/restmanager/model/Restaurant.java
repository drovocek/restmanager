package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.FilterJoinTables;
import org.hibernate.annotations.Formula;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Entity
@FilterJoinTables(
        @FilterJoinTable(name = "betweenDates", condition = ":menu_date <= startDate and :maxLength >= endDate")
)
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(name = "restaurants_unique_name_idx", columnNames = "name")})
public class Restaurant extends AbstractNamedEntity {

    @Setter
    @Getter
    @NotBlank
    @Size(min = 5, max = 200)
    @Column(name = "address", nullable = false)
    private String address;

    @Setter
    @Getter
    @NotBlank
//    https://www.baeldung.com/java-regex-validate-phone-numbers
    @Pattern(regexp = "^(\\+\\d{1}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
    @Size(max = 20)
    @Column(name = "phone", nullable = false)
    private String phone;

    @Setter
    @Getter
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @Setter
    @Getter
    @Formula("(SELECT COUNT(*) FROM Vote v WHERE v.restaurant_id = id)")
    private int votesQuantity;

    @OrderBy("menuDate DESC")
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Vote> votes;

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

    public List<Menu> getMenus() {
        return menus.stream().collect(Collectors.toList());
    }

    public void setMenus(Collection<Menu> menus) {
        this.menus = CollectionUtils.isEmpty(menus) ?
                Collections.emptyList() :
                menus.stream().collect(Collectors.toList());
    }
}
