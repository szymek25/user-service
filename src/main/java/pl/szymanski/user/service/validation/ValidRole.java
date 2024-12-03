package pl.szymanski.user.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.szymanski.user.service.constants.ApplicationConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
	String message() default ApplicationConstants.ValidationMessages.INVALID_ROLE;
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
