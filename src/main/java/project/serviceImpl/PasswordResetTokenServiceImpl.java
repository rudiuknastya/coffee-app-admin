package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.Admin;
import project.entity.PasswordResetToken;
import project.repository.AdminRepository;
import project.repository.PasswordResetTokenRepository;
import project.service.PasswordResetTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AdminRepository adminRepository;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, AdminRepository adminRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.adminRepository = adminRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");

    @Override
    public PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken) {
        logger.info("savePasswordResetToken() - Saving password reset token");
        PasswordResetToken passwordResetToken1 = passwordResetTokenRepository.save(passwordResetToken);
        logger.info("savePasswordResetToken() - Password reset token was saved");
        return passwordResetToken1;
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        logger.info("validatePasswordResetToken() - Finding password reset token and validating it");
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        boolean b;
        if(passwordResetToken == null || passwordResetToken.getExpirationDate().isBefore(LocalDateTime.now())){
            b = false;
        } else {
            b = true;
        }
        logger.info("validatePasswordResetToken() - Password reset token was found and validated");
        return b;
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        logger.info("getPasswordResetToken() - Finding password reset token");
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        logger.info("getPasswordResetToken() - Password reset token was found");
        return passwordResetToken;
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
