package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semato.semato_med.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
}
