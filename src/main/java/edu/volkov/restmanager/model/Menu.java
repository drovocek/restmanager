package edu.volkov.restmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends AbstractNamedEntity {

    @Column(name = "menu_date", nullable = false, columnDefinition = "data")
    @NotNull
    private LocalDate menuDate;

    @NotNull
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
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
