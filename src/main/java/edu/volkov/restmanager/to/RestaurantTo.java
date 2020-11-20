package edu.volkov.restmanager.to;

import edu.volkov.restmanager.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class RestaurantTo {

    private final Integer id;

    private final String name;

    private final String address;

    private final String phone;

    private final int likesAmount;

    private final List<Menu> menus;
}
