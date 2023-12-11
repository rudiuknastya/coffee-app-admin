package project.validators.phoneNumberValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import project.validators.emailValidation.FieldEmailExistValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Constraint(validatedBy = FieldPhoneNumberExistValidator.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldPhoneNumberExist {
    String message() default "Такий номер телефону вже існує";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
