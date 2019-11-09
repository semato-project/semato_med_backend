package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semato.semato_med.model.PasswordResetToken;
import semato.semato_med.model.User;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    Optional<PasswordResetToken> findByTokenAndUser(String token, User user);
}
