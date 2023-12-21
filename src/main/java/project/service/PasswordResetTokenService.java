package project.service;

import project.entity.Admin;
import project.entity.PasswordResetToken;

public interface PasswordResetTokenService {
    void updatePassword(String token,String password);
    boolean validatePasswordResetToken(String token);
    String createAndSavePasswordResetToken(Admin admin);
}
