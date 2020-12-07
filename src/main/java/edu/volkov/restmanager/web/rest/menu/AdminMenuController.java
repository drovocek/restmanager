package edu.volkov.restmanager.web.rest.menu;

import edu.volkov.restmanager.repository.menu.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL)
public class AdminMenuController {

    static final String REST_URL = "/rest/admin/menus";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuRepository repository;

    public AdminMenuController(MenuRepository repository) {
        this.repository = repository;
    }


}
