package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "menu_item")
public class MenuItem extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull
    private Menu menu;

    @Column(name = "price", nullable = false)
    private Integer price;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dish_id", nullable = false)
//    @NotNull
//    private Dish dish;

    public MenuItem() {
    }

    public MenuItem(MenuItem menuItem) {
        this(menuItem.getId(), menuItem.getName(), menuItem.getMenu(), menuItem.getPrice());
    }

    public MenuItem(Integer id, String name, Menu menu, Integer price) {
        super(id, name);
        this.menu = menu;
        this.price = price;
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
