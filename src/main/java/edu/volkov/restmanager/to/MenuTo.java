package edu.volkov.restmanager.to;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuTo extends BaseTo implements Serializable {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private LocalDate menuDate;

    @NotNull
    private boolean enabled;

    @NotNull
    private List<MenuItemTo> menuItemTos;

    public MenuTo(Integer id, String name, LocalDate menuDate, boolean enabled, List<MenuItemTo> menuItemTos) {
        super(id);
        this.name = name;
        this.menuDate = menuDate;
        this.enabled = enabled;
        this.menuItemTos = menuItemTos;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menuDate=" + menuDate +
                ", enabled=" + enabled +
                ", menuItems=" + menuItemTos +
                '}';
    }
}
