package team3.meowie.member.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameExistValidator.class)
public @interface ExistUsername {
	String message() default "User existed!";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
