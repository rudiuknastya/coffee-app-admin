package project.service;

import project.entity.PasswordResetToken;

public interface PasswordResetTokenService {
    PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken);
    boolean validatePasswordResetToken(String token);
    PasswordResetToken getPasswordResetToken(String token);
}
