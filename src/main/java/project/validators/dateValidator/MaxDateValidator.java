package project.validators.dateValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MaxDateValidator implements ConstraintValidator<MaxDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null){
            return true;
        }
        int year = date.getYear();
        int todayYear = LocalDate.now().getYear();
        todayYear = todayYear-18;
        return year <= todayYear;

    }
}
