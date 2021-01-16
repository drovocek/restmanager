package edu.volkov.restmanager.web.rest.menu;


import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.CrudMenuRepository;
import edu.volkov.restmanager.to.MenuTo;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


@Component
public class UniqueMenuInOneDateForRestValidator implements org.springframework.validation.Validator {

    private final CrudMenuRepository menuRepo;

    public UniqueMenuInOneDateForRestValidator(CrudMenuRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MenuTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MenuTo menu = ((MenuTo) target);
        Menu dbMenu = menuRepo.findById(menu.getId()).orElse(null);

        if (dbMenu != null &&
                !dbMenu.getMenuDate().equals(menu.getMenuDate()) &&
                !dbMenu.getRestaurant().getId().equals(menu.getId())) {
            errors.rejectValue("email", "exception.user.duplicateMenuInOneDate");
        }
    }
}
