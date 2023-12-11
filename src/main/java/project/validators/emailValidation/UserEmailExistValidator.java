package project.validators.emailValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import project.entity.User;
import project.repository.UserRepository;

import java.util.Optional;

public class UserEmailExistValidator implements ConstraintValidator<UserEmailExist,Object> {
    private final UserRepository userRepository;

    public UserEmailExistValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String id;
    private String email;
    @Override
    public void initialize(UserEmailExist constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.email = constraintAnnotation.email();
    }
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);
        Object emailValue = new BeanWrapperImpl(s).getPropertyValue(email);
        Optional<User> user = userRepository.findByEmail(emailValue.toString());
        if(user.isPresent() && !idValue.equals(user.get().getId())){
            return false;
        }
        return true;
    }
}
