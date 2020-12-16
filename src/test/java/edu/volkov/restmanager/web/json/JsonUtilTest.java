package edu.volkov.restmanager.web.json;

import edu.volkov.restmanager.model.Menu;
import org.junit.Test;

import java.util.List;

import static edu.volkov.restmanager.testdata.MenuTestData.*;

public class JsonUtilTest {

    @Test
    public void readWriteValue() throws Exception {
        String json = JsonUtil.writeValue(menu1WithItems);
        Menu menu = JsonUtil.readValue(json, Menu.class);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menu, menu1WithItems);
    }

    @Test
    public void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(rest1AllMenusWithItems);
        List<Menu> menus = JsonUtil.readValues(json, Menu.class);
        MENU_WITH_ITEMS_MATCHER.assertMatch(menus, rest1AllMenusWithItems);
    }
}