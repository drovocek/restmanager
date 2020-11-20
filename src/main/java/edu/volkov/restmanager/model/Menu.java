package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false, columnDefinition = "data")
    @NotNull
    private LocalDate menuDate = LocalDate.now();

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
//    private Set<MenuItem> menuItems;

    public Menu(Menu menu) {
        this(menu.getId(), menu.getName(), menu.getRestaurant(), menu.getMenuDate(), menu.isEnabled());
    }

    public Menu(Integer id, String name, Restaurant restaurant, LocalDate menuDate, boolean enabled) {
        super(id, name);
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.enabled = enabled;
    }
}
