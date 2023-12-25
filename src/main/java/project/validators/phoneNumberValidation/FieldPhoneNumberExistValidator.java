package project.validators.phoneNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.entity.Location;
import project.repository.LocationRepository;

import java.util.Optional;

public class FieldPhoneNumberExistValidator implements ConstraintValidator<FieldPhoneNumberExist,String> {
    private final LocationRepository locationRepository;

    public FieldPhoneNumberExistValidator(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Location> location = locationRepository.findByPhoneNumber(s);
        return location.isEmpty();

    }
}
