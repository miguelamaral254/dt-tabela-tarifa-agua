package gruporas.dttabelatarifaagua.shared.validation;

import gruporas.dttabelatarifaagua.user.persistence.model.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<ValidRole, Role> {

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle nulls
        }
        return Arrays.stream(Role.values())
                     .anyMatch(role -> role.equals(value));
    }
}
