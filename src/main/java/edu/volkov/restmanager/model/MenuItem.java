package edu.volkov.restmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.volkov.restmanager.View;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(name = "menu_item_unique_idx", columnNames = {"menu_id", "name"})})
public class MenuItem extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Min(value = 0)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull(groups = View.Persist.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Menu menu;

    public MenuItem() {
    }

    public MenuItem(MenuItem menuItem) {
        this(menuItem.getId(), menuItem.getName(), menuItem.getPrice());
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
