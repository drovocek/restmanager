package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends AbstractNamedEntity {

    @Getter
    @Setter
    @Column(name = "menu_date", nullable = false, columnDefinition = "data")
    @NotNull
    private LocalDate menuDate = LocalDate.now();

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    //@Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 200)
    private List<MenuItem> menuItems;

    public Menu(Menu menu) {
        this(menu.getId(), menu.getName(), menu.getMenuDate(), menu.isEnabled());
    }

    public Menu(Integer id, String name, LocalDate menuDate, boolean enabled) {
        super(id, name);
        this.menuDate = menuDate;
        this.enabled = enabled;
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

    public List<MenuItem> getMenuItems() {
        return menuItems.stream().collect(Collectors.toList());
    }

    public void setMenus(Collection<MenuItem> menuItems) {
        this.menuItems = CollectionUtils.isEmpty(menuItems) ?
                Collections.emptyList() :
                menuItems.stream().collect(Collectors.toList());
    }
}
