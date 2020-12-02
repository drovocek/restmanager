package edu.volkov.restmanager.web.json;

import edu.volkov.restmanager.model.Menu;
import org.junit.Test;

import java.util.List;

import static edu.volkov.restmanager.testdata.MenuTestData.*;

public class JsonUtilTest {

    @Test
    public void readWriteValue() throws Exception {
        String json = JsonUtil.writeValue(menu1);
        System.out.println(json);
        Menu menu = JsonUtil.readValue(json, Menu.class);
        MENU_MATCHER.assertMatch(menu, menu1);
    }

    @Test
    public void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(rest1Menus);
        System.out.println(json);
        List<Menu> menus = JsonUtil.readValues(json, Menu.class);
        MENU_MATCHER.assertMatch(menus, rest1Menus);
    }
}