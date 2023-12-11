package project.validators.emailValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.entity.Admin;
import project.entity.User;
import project.repository.AdminRepository;

import java.util.Optional;

public class FieldEmailExistValidator implements ConstraintValidator<FieldEmailExist,String> {
    private final AdminRepository adminRepository;

    public FieldEmailExistValidator(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Admin> admin = adminRepository.findByEmail(s);
        if(admin.isPresent()){
            return false;
        }
        return true;

    }
}
