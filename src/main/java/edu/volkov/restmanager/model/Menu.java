package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false, columnDefinition = "data")
    @NotNull
    private LocalDate menuDate = LocalDate.now();

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    private List<MenuItem> menuItems;

    public Menu(Menu menu) {
        this(menu.getId(), menu.getName(), menu.getRestaurant(), menu.getMenuDate(), menu.isEnabled());
    }

    public Menu(Integer id, String name, Restaurant restaurant, LocalDate menuDate, boolean enabled) {
        super(id, name);
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.enabled = enabled;
    }

    public Menu(Integer id, String name, Restaurant restaurant, LocalDate menuDate, boolean enabled, List<MenuItem> menuItems) {
        super(id, name);
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.enabled = enabled;
        setMenuItems(menuItems);
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems.stream().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Menu{" +
                "  id=" + id +
                ", name='" + name + '\'' +
                ", menuDate=" + menuDate +
                ", enabled=" + enabled +
                ", menuItems=" + menuItems +
                '}';
    }
}
