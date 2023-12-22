package project.validators.newPasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
@Constraint(validatedBy = NewPasswordValidValidator.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewPasswordValid {
    String message() default "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
