package edu.volkov.restmanager.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemTo extends BaseTo implements Serializable {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    @Min(value = 0)
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
