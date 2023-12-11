package project.validators.phoneNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import project.entity.Location;
import project.entity.User;
import project.repository.LocationRepository;

import java.util.Optional;

public class LocationPhoneNumberExistValidator implements ConstraintValidator<LocationPhoneNumberExist,Object> {
    private final LocationRepository locationRepository;

    public LocationPhoneNumberExistValidator(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    private String id;
    private String phoneNumber;
    @Override
    public void initialize(LocationPhoneNumberExist constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.phoneNumber = constraintAnnotation.phoneNumber();
    }
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);
        Object phoneValue = new BeanWrapperImpl(s).getPropertyValue(phoneNumber);
        Optional<Location> location = locationRepository.findByPhoneNumber(phoneValue.toString());
        if(location.isPresent() && !idValue.equals(location.get().getId())){
            return false;
        }
        return true;
    }
}
