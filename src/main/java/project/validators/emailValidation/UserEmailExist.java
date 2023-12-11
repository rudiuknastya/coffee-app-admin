package project.validators.emailValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = UserEmailExistValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailExist {
    String id();
    String email();
    String message() default "Така пошта вже існує";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
