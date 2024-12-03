package pl.szymanski.user.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.user.service.service.RoleService;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

	@Autowired
	private RoleService roleService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotEmpty(value)) {
			return roleService.getById(value) != null;
		} else {
			// skip checking for empty value as @NotEmpty is used for the field
			return true;
		}
	}
}
