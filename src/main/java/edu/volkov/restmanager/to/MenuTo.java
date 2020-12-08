package edu.volkov.restmanager.to;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuTo extends BaseTo implements Serializable {

    private String name;

    private LocalDate menuDate;

    private boolean enabled;

    private Integer restId;

    private List<MenuItemTo> menuItemTos;

    public MenuTo(Integer id, String name, LocalDate menuDate, boolean enabled, Integer restId, List<MenuItemTo> menuItemTos) {
        super(id);
        this.name = name;
        this.menuDate = menuDate;
        this.enabled = enabled;
        this.restId = restId;
        this.menuItemTos = menuItemTos;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menuDate=" + menuDate +
                ", enabled=" + enabled +
                ", restId=" + restId +
                ", menuItems=" + menuItemTos +
                '}';
    }
}
