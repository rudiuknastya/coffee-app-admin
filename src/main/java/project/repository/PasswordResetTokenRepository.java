package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
