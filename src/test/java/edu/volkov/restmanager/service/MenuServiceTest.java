package edu.volkov.restmanager.service;

import edu.volkov.restmanager.model.Menu;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static edu.volkov.restmanager.MenuTestData.*;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Test
    public void getBetween() {
        List<Menu> actual = service.getBetween(null, null);
        MENU_MATCHER.assertMatch(actual,allMenus);
    }

    @Test
    public void getByPreassignedQuantity() {

    }

}
