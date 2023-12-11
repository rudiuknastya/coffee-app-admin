package project.validators.emailValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import project.entity.Admin;
import project.repository.AdminRepository;
import java.util.Optional;

public class EmailExistValidator implements ConstraintValidator<EmailExist,Object> {
    private final AdminRepository adminRepository;

    public EmailExistValidator(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    private String id;
    private String email;
    @Override
    public void initialize(EmailExist constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.email = constraintAnnotation.email();
    }
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);
        Object emailValue = new BeanWrapperImpl(s).getPropertyValue(email);
        Optional<Admin> admin = adminRepository.findByEmail(emailValue.toString());
        if(admin.isPresent() && idValue.equals(admin.get().getId())){
            return false;
        }
        return true;

    }
}
