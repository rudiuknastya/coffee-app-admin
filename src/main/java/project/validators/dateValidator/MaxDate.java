package project.validators.dateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import project.validators.phoneNumberValidation.FieldPhoneNumberExistValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Constraint(validatedBy = MaxDateValidator.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxDate {
    String message() default "Невірна дата";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
