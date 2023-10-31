package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.PasswordResetToken;
import project.repository.PasswordResetTokenRepository;
import project.service.PasswordResetTokenService;

import java.time.LocalDateTime;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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
}
