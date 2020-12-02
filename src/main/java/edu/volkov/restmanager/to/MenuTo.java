package edu.volkov.restmanager.to;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class MenuTo {

    private final Integer id;

    private final String name;

    private final LocalDate menuDate;

    private final boolean enabled;

    private final Integer restId;

    private final List<MenuItemTo> menuItems;
}
