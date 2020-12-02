package edu.volkov.restmanager.to;

import edu.volkov.restmanager.model.Menu;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class RestaurantTo {

    private final Integer id;

    private final String name;

    private final String address;

    private final String phone;

    private final int likesAmount;

    private final List<Menu> menus;

    @ConstructorProperties({"id", "name", "address", "phone", "likesAmount", "menus"})
    public RestaurantTo(Integer id, String name, String address, String phone, int likesAmount, List<Menu> menus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.likesAmount = likesAmount;
        this.menus = menus;
    }
}
