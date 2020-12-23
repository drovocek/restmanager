package edu.volkov.restmanager.web.rest.user;


import edu.volkov.restmanager.HasIdAndEmail;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.repository.CrudUserRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;


@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final CrudUserRepository repository;

    public UniqueMailValidator(CrudUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
            if (dbUser != null && !dbUser.getId().equals(user.getId())) {
                errors.rejectValue("email", "exception.user.duplicateEmail");
            }
        }
    }
}
