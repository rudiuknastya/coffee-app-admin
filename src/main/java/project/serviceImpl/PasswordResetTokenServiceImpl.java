package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.entity.Admin;
import project.entity.PasswordResetToken;
import project.repository.AdminRepository;
import project.repository.PasswordResetTokenRepository;
import project.service.PasswordResetTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void updatePassword(String token, String password) {
        logger.info("updatePassword() - Updating password");
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new EntityNotFoundException("Password reset token was not found by token "+token));
        passwordResetToken.getAdmin().setPassword(passwordEncoder.encode(password));
        passwordResetTokenRepository.save(passwordResetToken);
        logger.info("updatePassword() - Password was updated");
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        logger.info("validatePasswordResetToken() - Finding password reset token and validating it");
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
        boolean isValid = passwordResetToken.isPresent() && !passwordResetToken.get().getExpirationDate().isBefore(LocalDateTime.now());
        logger.info("validatePasswordResetToken() - Password reset token was found and validated");
        return isValid;
    }


    @Override
    public String createAndSavePasswordResetToken(Admin admin) {
        logger.info("createAndSavePasswordResetToken() - Creating and saving password reset token");
        String token = UUID.randomUUID().toString();
        if(admin.getPasswordResetToken() != null){
            admin.getPasswordResetToken().setToken(token);
            admin.getPasswordResetToken().setExpirationDate();
            adminRepository.save(admin);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, admin);
            passwordResetTokenRepository.save(passwordResetToken);
        }
        logger.info("createAndSavePasswordResetToken() - Password reset token was created and saved");
        return token;
    }
}
