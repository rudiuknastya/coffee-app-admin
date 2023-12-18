package project.validators.notEmptyPasswords;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
@Constraint(validatedBy = NotEmptyOldPasswordValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyOldPasswordPassword {
    String oldPassword();
    String newPassword();

    String message() default "Поле не може бути порожнім";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
