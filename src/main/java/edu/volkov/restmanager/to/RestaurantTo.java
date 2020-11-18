package edu.volkov.restmanager.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class RestaurantTo {

    private final Integer id;

    private final String name;

    private final String address;

    private final String phone;

    private final int likesAmount;
}
