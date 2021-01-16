package edu.volkov.restmanager.model;

import edu.volkov.restmanager.HasIdAndName;
import edu.volkov.restmanager.View;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(name = "restaurants_unique_name_idx", columnNames = "name")})
public class Restaurant extends AbstractNamedEntity implements HasIdAndName {

    @NotBlank
    @Size(min = 5, max = 200)
    @Column(name = "address", nullable = false)
    @SafeHtml(groups = {View.Web.class}, whitelistType = NONE)
    private String address;

    @NotBlank
//    https://www.baeldung.com/java-regex-validate-phone-numbers
    @Pattern(regexp = "^(\\+\\d{1}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "must be in format: +7 (777) 777-7777")
    @Size(max = 20)
    @Column(name = "phone", nullable = false)
    @SafeHtml(groups = {View.Web.class}, whitelistType = NONE)
    private String phone;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @Formula("(SELECT COUNT(*) FROM Vote v WHERE v.restaurant_id = id)")
    private int votesQuantity;

    @OrderBy("menuDate DESC")
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();

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

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Collection<Menu> menus) {
        this.menus = CollectionUtils.isEmpty(menus) ?
                Collections.emptyList() :
                new ArrayList<>(menus);
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
