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

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default false")
    private boolean enabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull
    private Menu menu;

    public MenuItem() {
    }

    public MenuItem(MenuItem menuItm) {
        this(menuItm.getId(), menuItm.getName(), menuItm.isEnabled(), menuItm.getPrice());
    }

    public MenuItem(Integer id, String name, Boolean enabled, Integer price) {
        super(id, name);
        this.enabled = enabled;
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
