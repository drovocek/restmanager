package edu.volkov.restmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.volkov.restmanager.View;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(name = "menu_unique_idx", columnNames = {"restaurant_id", "menu_date"})})
public class Menu extends AbstractNamedEntity {

    @Column(name = "menu_date", nullable = false, columnDefinition = "data")
    @NotNull
    private LocalDate menuDate;

    @NotNull
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = View.Persist.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu",cascade = CascadeType.ALL)
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
        return menuItems;
    }

    public void setMenus(Collection<MenuItem> menuItems) {
        this.menuItems = CollectionUtils.isEmpty(menuItems) ?
                Collections.emptyList() :
                new ArrayList<>(menuItems);
    }
}
