package project.validators.phoneNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import project.entity.User;
import project.repository.UserRepository;

import java.util.Optional;

public class PhoneNumberExistValidator implements ConstraintValidator<PhoneNumberExist,Object> {
    private final UserRepository userRepository;

    public PhoneNumberExistValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String id;
    private String phoneNumber;
    @Override
    public void initialize(PhoneNumberExist constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.phoneNumber = constraintAnnotation.phoneNumber();
    }
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);
        Object phoneValue = new BeanWrapperImpl(s).getPropertyValue(phoneNumber);
        Optional<User> user = userRepository.findByPhoneNumber(phoneValue.toString());
        if(user.isPresent() && !idValue.equals(user.get().getId())){
            return false;
        }
        return true;
    }
}
