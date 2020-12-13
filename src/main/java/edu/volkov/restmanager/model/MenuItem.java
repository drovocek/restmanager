package edu.volkov.restmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "menu_item")
public class MenuItem extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull
    @JsonIgnore
    private Menu menu;

    public MenuItem() {
    }

    public MenuItem(MenuItem menuItm) {
        this(menuItm.getId(), menuItm.getName(), menuItm.getPrice());
    }

    public MenuItem(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    public MenuItem(Integer id, String name, Integer price, Menu menu) {
        super(id, name);
        this.price = price;
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
