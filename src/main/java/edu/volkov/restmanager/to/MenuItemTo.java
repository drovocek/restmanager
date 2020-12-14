package edu.volkov.restmanager.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemTo extends BaseTo implements Serializable {

    private String name;

    private Integer price;

    public MenuItemTo(Integer id, String name, Integer price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "MenuItemTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
