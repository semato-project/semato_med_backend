package semato.semato_med.models.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import semato.semato_med.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
