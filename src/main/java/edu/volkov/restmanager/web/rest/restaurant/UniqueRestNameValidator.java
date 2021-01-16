package edu.volkov.restmanager.web.rest.restaurant;


import edu.volkov.restmanager.HasIdAndName;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.repository.CrudRestaurantRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;


@Component
public class UniqueRestNameValidator implements org.springframework.validation.Validator {

    private final CrudRestaurantRepository repository;

    public UniqueRestNameValidator(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndName.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndName bean = ((HasIdAndName) target);
        if (StringUtils.hasText(bean.getName())) {
            HasIdAndName dbBean= repository.getByName(bean.getName().toLowerCase());
            if (dbBean != null && !dbBean.getId().equals(bean.getId())) {
                errors.rejectValue("email", "exception.user.duplicateName");
            }
        }
    }
}
