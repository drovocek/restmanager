package edu.volkov.restmanager.to;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemTo extends BaseTo implements Serializable {

    private String name;

    private Integer price;

    private Integer menuId;

    public MenuItemTo(Integer id, String name, Integer price, Integer menuId) {
        super(id);
        this.name = name;
        this.price = price;
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "MenuItemTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuId=" + menuId +
                '}';
    }
}
